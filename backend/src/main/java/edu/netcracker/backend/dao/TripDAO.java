package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;

import java.util.List;
import java.util.Optional;

public interface TripDAO {

    void save(Trip trip);

    Optional<Trip> find(Number id);

    void delete(Trip trip);

    List<TripWithArrivalAndDepartureData> getAllTripsWitArrivalAndDepatureDataBelongToCarrier(Number carrierId);
}
