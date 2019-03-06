package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.TripState;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripDAO tripDAO;
    private final TripStateRegistry tripStateRegistry;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO, TripStateRegistry tripStateRegistry) {
        this.tripDAO = tripDAO;
        this.tripStateRegistry = tripStateRegistry;
    }

    @Override
    public TripDTO resolveTrip(User requestUser, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripDAO.find(tripDTO.getTripId());

        if(!optionalTrip.isPresent()){
            return createTrip(requestUser, tripDTO);
        }
        else{
            return updateTrip(requestUser, optionalTrip.get(), tripDTO);
        }
    }

    private TripDTO updateTrip(User requestUser, Trip trip, TripDTO tripDTO) {
        TripState dtoState = tripStateRegistry.getState(tripDTO.getStatus());

        if(!dtoState.equals(trip.getTripState())){
            changeStatus(requestUser, trip, dtoState, tripDTO);
        }

        tripDAO.save(trip);
        return TripDTO.from(trip);
    }

    private void changeStatus(User requestUser, Trip trip, TripState tripState, TripDTO tripDTO) {
        if(!trip.changeStatus(requestUser, tripState, tripDTO)){
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private TripDTO createTrip(User requestUser, TripDTO tripDTO) {
        // todo
        return null;
    }
}