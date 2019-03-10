package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.*;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripDAO tripDAO;
    private final TripStateRegistry tripStateRegistry;
    private final Removed removed;
    private final Open open;
    private final Assigned assigned;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO,
                           TripStateRegistry tripStateRegistry,
                           Removed removed,
                           Open open,
                           Assigned assigned) {
        this.tripDAO = tripDAO;
        this.tripStateRegistry = tripStateRegistry;
        this.removed = removed;
        this.open = open;
        this.assigned = assigned;
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
        if (status == removed.getDatabaseValue()) {
            return new ArrayList<>();
        }

        return tripDAO.findAllByCarrierAndStatus(requestUser.getUserId(), status, offset, limit);
    }

    @Override
    public List<Trip> findCarrierTrips(User requestUser, Long offset, Long limit) {
        return tripDAO.findAllByCarrier(requestUser.getUserId(), removed.getDatabaseValue(), offset, limit);
    }

    @Override
    public List<Trip> findApproverTrips(User requestUser, Integer status, Long offset, Long limit) {
        if (status == open.getDatabaseValue()) {
            return tripDAO.findAllByStatus(status, offset, limit);
        }
        if (status == assigned.getDatabaseValue()) {
            return tripDAO.findAllByApproverByStatus(requestUser.getUserId(), status, offset, limit);
        }
        throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
    }

    private Trip updateTrip(User requestUser, Trip trip, TripDTO tripDTO) {
        TripState dtoState = tripStateRegistry.getState(tripDTO.getStatus());

        if (!dtoState.equals(trip.getTripState())) {
            changeStatus(requestUser, trip, dtoState, tripDTO);
        }

        tripDAO.save(trip);
        return trip;
    }

    private void changeStatus(User requestUser, Trip trip, TripState tripState, TripDTO tripDTO) {
        if (!trip.changeStatus(requestUser, tripState, tripDTO)) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }
}