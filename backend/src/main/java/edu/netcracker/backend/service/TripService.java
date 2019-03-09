package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.TripCreation;
import edu.netcracker.backend.model.Trip;

public interface TripService {
    public void saveTrip(TripCreation tripCreation);
}
