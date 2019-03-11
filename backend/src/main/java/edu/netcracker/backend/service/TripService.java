package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.message.response.trips.AllCarriersTripsDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

import java.util.List;

public interface TripService {

    void saveTrip(TripCreation tripCreation);

    List<AllCarriersTripsDTO> getAllTripsForCarrier();

    Trip updateTrip(User requestUser, TripDTO tripDTO);
}
