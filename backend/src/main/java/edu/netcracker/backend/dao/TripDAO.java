package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface TripDAO {
    void save(Trip trip);

    Optional<Trip> find(Long id);

    List<Trip> allCarriersTrips(Long carrierId);

    List<Trip> paginationForCarrier(Integer limit, Integer offset, Long carrierId);

    List<Trip> findByStatusForCarrier(Integer status, Long carrierId);

    List<Trip> findByPlanetsForCarrier(String departurePlanet, String arrivalPlanet, Long carrierId);

    void update(Trip trip);

    void delete(Trip trip);
}
