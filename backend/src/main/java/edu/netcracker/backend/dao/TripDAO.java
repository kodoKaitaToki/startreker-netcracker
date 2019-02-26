package edu.netcracker.backend.dao;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TripDAO {

    void save(Trip trip);

    Optional<Trip> find(Number id);

    List<Trip> findAllTripsForCarrier(Long carrier_id);

    List<Trip> findByDepartureDate(Long carrier_id, LocalDate departureDate);

    List<Trip> findByArrivalDate(Long carrier_id, LocalDate arrivalDate);

    List<Trip> findPerDeparturePeriod(Long carrier_id, LocalDateTime from, LocalDateTime to);

    List<Trip> findPerArrivalPeriod(Long carrier_id, LocalDateTime from, LocalDateTime to);

    List<Trip> findByStatus(Long carrier_id, Number status);

    List<Trip> findByCreationDate(Long carrier_id, LocalDate creationDate);

    List<Trip> findPerCreationPeriod(Long carrier_id, LocalDate from, LocalDate to);

    List<Trip> findByDeparturePlanet(Long carrier_id, String planet_name);

    List<Trip> findByArrivalPlanet(Long carrier_id, String planet_name);

    List<Trip> pagination(Long carrier_id, Number number, Number from);

    void delete(Trip trip);

}