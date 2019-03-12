package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.message.response.TripResponse;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.*;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripDAO tripDAO;
    private final ApplicationContext applicationContext;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO, ApplicationContext applicationContext) {
        this.tripDAO = tripDAO;
        this.applicationContext = applicationContext;
    }

    @Override
    public Trip updateTrip(User requestUser, TripRequest tripRequest) {
        Optional<Trip> optionalTrip = tripDAO.find(tripRequest.getTripId());

        if (!optionalTrip.isPresent()) {
            throw new RequestException("Illegal operation", HttpStatus.NOT_FOUND);
        } else {
            return updateTrip(requestUser, optionalTrip.get(), tripRequest);
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

    private Trip updateTrip(User requestUser, Trip trip, TripRequest tripRequest) {
        TripState desiredState = TripState.getState(tripRequest.getStatus());

        if (!desiredState.equals(trip.getTripState())) {
            startStatusChange(requestUser, trip, desiredState, tripRequest);
        }

        tripDAO.save(trip);
        return tripDAO.find(trip.getTripId()).orElseThrow(RequestException::new);
    }

    private void startStatusChange(User requestUser, Trip trip, TripState tripState, TripRequest tripRequest) {
        if (!changeStatus(requestUser, tripState, tripRequest, trip)) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private boolean changeStatus(User requestUser, TripState newTripState, TripRequest tripRequest, Trip trip) {
        if (requestUser == null
            || newTripState == null
            || trip.getTripState() == null
            || !newTripState.isStateChangeAllowed(trip, requestUser)) {
            return false;
        }

        if (newTripState.apply(applicationContext, trip, tripRequest, requestUser)) {
            trip.setTripState(newTripState);
            return true;
        }

        return false;
    }
}