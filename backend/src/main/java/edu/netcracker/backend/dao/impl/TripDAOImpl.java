package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.VehicleDAO;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sql/tripdao.properties")
public class TripDAOImpl extends CrudDAOImpl<Trip> implements TripDAO {

    private TicketClassDAO ticketClassDAO;
    private VehicleDAO vehicleDAO;

    @Value("${FIND_ALL_TICKET_TRIPS}")
    private String FIND_ALL_TICKET_TRIPS;

    @Value("${FIND_ALL_TRIPS_FOR_CARRIER}")
    private String FIND_ALL_TRIPS_FOR_CARRIER;

    @Value("${FIND_BY_DEPARTURE_DATE}")
    private String FIND_BY_DEPARTURE_DATE;

    @Value("${FIND_BY_ARRIVAL_DATE}")
    private String FIND_BY_ARRIVAL_DATE;

    @Value("${FIND_PER_DEPARTURE_PERIOD}")
    private String FIND_PER_DEPARTURE_PERIOD;

    @Value("${FIND_PER_ARRIVAL_PERIOD}")
    private String FIND_PER_ARRIVAL_PERIOD;

    @Value("${FIND_BY_STATUS}")
    private String FIND_BY_STATUS;

    @Value("${FIND_BY_CREATION_DATE}")
    private String FIND_BY_CREATION_DATE;

    @Value("${FIND_PER_CREATION_PERIOD}")
    private String FIND_PER_CREATION_PERIOD;

    @Value("${FIND_BY_DEPARTURE_PLANET}")
    private String FIND_BY_DEPARTURE_PLANET;

    @Value("${FIND_BY_ARRIVAL_PLANET}")
    private String FIND_BY_ARRIVAL_PLANET;

    @Value("${PAGINATION}")
    private String PAGINATION;

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO, VehicleDAO vehicleDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.vehicleDAO = vehicleDAO;
    }

    @Override
    public List<Trip> findAllTripsForCarrier(Long carrier_id) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_ALL_TRIPS_FOR_CARRIER,
                new Object[]{carrier_id},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByDepartureDate(Long carrier_id, LocalDate departureDate) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_BY_DEPARTURE_DATE,
                new Object[]{carrier_id, departureDate},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByArrivalDate(Long carrier_id, LocalDate arrivalDate) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_PER_DEPARTURE_PERIOD,
                new Object[]{carrier_id, arrivalDate},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findPerDeparturePeriod(Long carrier_id, LocalDateTime from, LocalDateTime to) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_PER_DEPARTURE_PERIOD,
                new Object[]{carrier_id, from, to},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findPerArrivalPeriod(Long carrier_id, LocalDateTime from, LocalDateTime to) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_PER_ARRIVAL_PERIOD,
                new Object[]{carrier_id, from, to},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByStatus(Long carrier_id, Number status) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_BY_STATUS,
                new Object[]{carrier_id, status},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByCreationDate(Long carrier_id, LocalDate creationDate) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_BY_CREATION_DATE,
                new Object[]{carrier_id, creationDate},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findPerCreationPeriod(Long carrier_id, LocalDate from, LocalDate to) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_PER_CREATION_PERIOD,
                new Object[]{carrier_id, from, to},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByDeparturePlanet(Long carrier_id, String planet_name) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_BY_DEPARTURE_PLANET,
                new Object[]{carrier_id, planet_name},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> findByArrivalPlanet(Long carrier_id, String planet_name) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                FIND_BY_ARRIVAL_PLANET,
                new Object[]{carrier_id, planet_name},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }

    @Override
    public List<Trip> pagination(Long carrier_id, Number number, Number from) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                PAGINATION,
                new Object[]{carrier_id, number, from},
                getGenericMapper()));

        trips.forEach(this::attachTicketClassed);
        return trips;
    }


    @Override
    public Optional<Trip> find(Number id) {
        Optional<Trip> tripOpt = super.find(id);
        return tripOpt.flatMap(this::attachTicketClassed);
    }

    private Optional<Trip> attachTicketClassed(Trip trip) {
        List<Long> rows = getJdbcTemplate().queryForList(FIND_ALL_TICKET_TRIPS, Long.class, trip.getTripId());
        trip.setTicketClasses(ticketClassDAO.findIn(rows));
        return Optional.of(trip);
    }

}
