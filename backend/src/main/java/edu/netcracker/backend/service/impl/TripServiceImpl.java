package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.*;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripDAO tripDAO;
    private final ApplicationContext applicationContext;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO,
                           ApplicationContext applicationContext) {
        this.tripDAO = tripDAO;
        this.applicationContext = applicationContext;
    }

    @Override
    public Trip updateTrip(User requestUser, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripDAO.find(tripDTO.getTripId());

        if (!optionalTrip.isPresent()) {
            throw new RequestException("Illegal operation", HttpStatus.NOT_FOUND);
        } else {
            return updateTrip(requestUser, optionalTrip.get(), tripDTO);
        }
    }

    @Override
    public List<Trip> findCarrierTripsByStatus(User requestUser, Integer status, Long offset, Long limit) {
        if (status == TripState.REMOVED.getDatabaseValue()) {
            return new ArrayList<>();
        }

        return tripDAO.findAllByCarrierAndStatus(requestUser.getUserId(), status, offset, limit);
    }

    @Override
    public List<Trip> findCarrierTrips(User requestUser, Long offset, Long limit) {
        return tripDAO.findAllByCarrier(requestUser.getUserId(), TripState.REMOVED.getDatabaseValue(), offset, limit);
    }

    @Override
    public List<Trip> findApproverTrips(User requestUser, Integer status, Long offset, Long limit) {
        if (status == TripState.OPEN.getDatabaseValue()) {
            return tripDAO.findAllByStatus(status, offset, limit);
        }
        if (status == TripState.ASSIGNED.getDatabaseValue()) {
            return tripDAO.findAllByApproverByStatus(requestUser.getUserId(), status, offset, limit);
        }
        throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
    }

    private Trip updateTrip(User requestUser, Trip trip, TripDTO tripDTO) {
        TripState desiredState = TripState.getState(tripDTO.getStatus());

        if (!desiredState.equals(trip.getTripState())) {
            startStatusChange(requestUser, trip, desiredState, tripDTO);
        }

        tripDAO.save(trip);
        return trip;
    }

    private void startStatusChange(User requestUser, Trip trip, TripState tripState, TripDTO tripDTO) {
        if (!changeStatus(requestUser, tripState, tripDTO, trip)) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private boolean changeStatus(User requestUser, TripState newTripState, TripDTO tripDTO, Trip trip) {
        if (requestUser == null
            || newTripState == null
            || trip.getTripState() == null
            || !newTripState.isStateChangeAllowed(trip, requestUser)) {
            return false;
        }

        if (newTripState.getAction()
                        .apply(applicationContext, trip, tripDTO, requestUser)) {
            trip.setTripState(newTripState);
            return true;
        }

        return false;
    }
}