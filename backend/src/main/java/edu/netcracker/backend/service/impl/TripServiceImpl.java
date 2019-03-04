package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.TripState;
import edu.netcracker.backend.model.state.trip.TripStateUtils;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripDAO tripDAO;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO) {
        this.tripDAO = tripDAO;
    }

    @Override
    public Trip changeStatus(User requestUser, long tripId, int stateId) {
        Trip trip = tripDAO.find(tripId).orElseThrow(
                () -> new RequestException("Trip " + tripId + " not found ", HttpStatus.NOT_FOUND));
        tripDAO.save(doChangeStatus(requestUser, trip, TripStateUtils.getState(stateId)));
        return trip;
    }

    @Override
    public TripDTO resolveTrip(User requestUser, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripDAO.find(tripDTO.getTripId());
        if(!optionalTrip.isPresent()){
            return createTrip(requestUser, tripDTO);
        }
        Trip trip = optionalTrip.get();
        TripState dtoState = TripStateUtils.getState(tripDTO.getStatus());
        if(!dtoState.equals(trip.getTripState())){
            doChangeStatus(requestUser, trip, dtoState);
        }
        tripDAO.save(trip);
        return TripDTO.from(trip);
    }

    private TripDTO createTrip(User requestUser, TripDTO tripDTO) {
        // todo
        return null;
    }

    private Trip doChangeStatus(User requestUser, Trip trip, TripState tripState) {
        if(trip.changeStatus(requestUser, tripState)){
            return trip;
        } else {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }
}