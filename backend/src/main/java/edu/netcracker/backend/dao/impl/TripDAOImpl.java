package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TripDAOImpl extends CrudDAO<Trip> implements TripDAO {
    private TicketClassDAO ticketClassDAO;

    private final String findAllTicketTrips = "SELECT class_id FROM ticket_class WHERE trip_id = ?";
    private final String findAllTripsForCarrierSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ?";
    private final String findByDepartureDateSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND departure_date = ?";
    private final String findByArrivalDateSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND arrival_date = ?";
    private final String findPerDeparturePeriodSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND departure_date BETWEEN ? AND ?";
    private final String findPerArrivalPeriodSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND arrival_date BETWEEN ? AND ?";
    private final String findByStatusSQL = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND trip_status = ?";
    private final String findByCreationDateSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND t.creation_date = ?";
    private final String findPerCreationPeriodSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND t.creation_date BETWEEN ? AND ?";
    private final String findByDeparturePlanetSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND dp.planet_name = ?";
    private final String findByArrivalPlanetSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? AND ap.planet_name = ?";
    private final String paginationSql = "SELECT trip_id, trip_status," +
            "departure_date, dp.planet_name AS departure_planet, ds.spaceport_name AS departure_spaceport," +
            "arrival_date, ap.planet_name AS arrival_planet, ars.spaceport_name AS arrival_spaceport," +
            "trip_photo, t.creation_date, vehicle_name" +
            "FROM trip AS t " +
            "INNER JOIN vehicle AS v ON t.vehicle_id = v.vehicle_id" +
            "INNER JOIN spaceport AS ds ON departure_id = ds.spaceport_id" +
            "INNER JOIN planet AS dp ON ds.planet_id = dp.planet_id" +
            "INNER JOIN spaceport AS ars ON t.arrival_id = ars.spaceport_id" +
            "INNER JOIN planet AS ap ON ars.planet_id = ap.planet_id" +
            "WHERE owner_id = ? ORDER BY departure_date DESC LIMIT ? OFFSET ?";

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO) {
        this.ticketClassDAO = ticketClassDAO;
    }

    @Override
    public Optional<Trip> find(Number id) {
        Optional<Trip> tripOpt = super.find(id);

        if (tripOpt.isPresent()) {
            return attachTicketClassed(tripOpt.get());
        }
        return Optional.empty();
    }

    @Override
    public List<Trip> findAllTripsForCarrier(Long carrier_id) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findAllTripsForCarrierSql,
                new Object[]{carrier_id},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByDepartureDate(Long carrier_id, LocalDate departureDate) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findByDepartureDateSql,
                new Object[]{carrier_id, departureDate},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByArrivalDate(Long carrier_id, LocalDate arrivalDate) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findByArrivalDateSql,
                new Object[]{carrier_id, arrivalDate},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findPerDeparturePeriod(Long carrier_id, LocalDateTime from, LocalDateTime to) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findPerDeparturePeriodSql,
                new Object[]{carrier_id, from, to},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findPerArrivalPeriod(Long carrier_id, LocalDateTime from, LocalDateTime to) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findPerArrivalPeriodSql,
                new Object[]{carrier_id, from, to},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByStatus(Long carrier_id, Number status) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findByStatusSQL,
                new Object[]{carrier_id, status},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByCreationDate(Long carrier_id, LocalDate creationDate) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findByCreationDateSql,
                new Object[]{carrier_id, creationDate},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findPerCreationPeriod(Long carrier_id, LocalDate from, LocalDate to) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findPerCreationPeriodSql,
                new Object[]{carrier_id, from, to},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByDeparturePlanet(Long carrier_id, String planet_name) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findByDeparturePlanetSql,
                new Object[]{carrier_id, planet_name},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByArrivalPlanet(Long carrier_id, String planet_name) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findByArrivalPlanetSql,
                new Object[]{carrier_id, planet_name},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> pagination(Long carrier_id, Number number, Number from) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                paginationSql,
                new Object[]{carrier_id, number, from},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    private Optional<Trip> attachTicketClassed(Trip trip) {
        List<Long> rows = getJdbcTemplate().queryForList(findAllTicketTrips, Long.class, trip.getTripId());
        List<TicketClass> ticketClasses = new ArrayList<>();

        for (Long ticket_class_id : rows) {
            ticketClasses.add(ticketClassDAO.find(ticket_class_id).orElse(null));
        }

        trip.setTicketClasses(ticketClasses);
        return Optional.of(trip);
    }

}
