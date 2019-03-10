package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.trips.AllCarriersTripsDTO;

import java.util.List;

public interface TripService {
    public void saveTrip(TripCreation tripCreation);

    public List<AllCarriersTripsDTO> getAllTripsForCarrier();
}
