package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

public interface TripService {

    Trip changeStatus(User requestUser, long id, int stateId);
    TripDTO resolveTrip(User requestUser, TripDTO tripDTO);
}
