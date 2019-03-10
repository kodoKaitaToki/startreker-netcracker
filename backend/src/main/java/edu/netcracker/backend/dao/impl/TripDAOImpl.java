package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.mapper.TripCRUDMapper;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.state.trip.Draft;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sql/tripdao.properties")
public class TripDAOImpl extends CrudDAOImpl<Trip> implements TripDAO {

    @Autowired
    private TripStateRegistry tripStateRegistry;

    private TicketClassDAO ticketClassDAO;

    @Value("${FIND_ALL_TICKET_TRIPS}")
    private String FIND_ALL_TICKET_TRIPS;

    private String FIND_ALL_TRIPS_FOR_CARRIER = "SELECT trip_id, carrier_id, trip_status, " +
            "ds.spaceport_id AS departure_spaceport_id, ds.spaceport_name AS departure_spaceport_name, " +
            "dp.planet_id AS departure_planet_id, dp.planet_name AS departure_planet_name, departure_date, " +
            "ars.spaceport_id AS arrival_spaceport_id, ars.spaceport_name AS arrival_spaceport_name, " +
            "arp.planet_id AS arrival_planet_id, arp.planet_name AS arrival_planet_name, arrival_date, " +
            "trip_photo, approver_id, t.creation_date " +
            "FROM trip as t " +
            "INNER JOIN spaceport AS ds ON ds.spaceport_id = t.departure_id " +
            "INNER JOIN planet AS dp ON dp.planet_id = ds.planet_id " +
            "INNER JOIN spaceport AS ars ON ars.spaceport_id = t.arrival_id " +
            "INNER JOIN planet AS arp ON arp.planet_id = ars.planet_id " +
            "WHERE carrier_id = ? AND trip_status != 7 ";

    private String PAGINATION = "ORDER BY t.creation_date DESC LIMIT ? OFFSET ?";

    private String ALL_TRIPS_PAGINATION = FIND_ALL_TRIPS_FOR_CARRIER + PAGINATION;

    private String FIND_BY_STATUS = FIND_ALL_TRIPS_FOR_CARRIER + "AND trip_status = ? ";

    private String FIND_BY_PLANETS = FIND_ALL_TRIPS_FOR_CARRIER + "AND dp.planet_name = UPPER(?) " +
            "AND arp.planet_name = UPPER(?) ";

    private String INSERT_TRIP = "INSERT INTO TRIP (carrier_id, trip_status, trip_photo, departure_id, departure_date, " +
            "arrival_id, arrival_date, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO) {
        this.ticketClassDAO = ticketClassDAO;
    }

    @Override
    public Optional<Trip> find(Long id) {
        Optional<Trip> tripOpt = super.find(id);
        return tripOpt.flatMap(this::attachTicketClasses);
    }

    @Override
    public List<Trip> allCarriersTrips(Long carrierId) {
        List<Trip> trips = getJdbcTemplate()
                .query(FIND_ALL_TRIPS_FOR_CARRIER,
                        new Object[]{carrierId},
                        new TripCRUDMapper(this.tripStateRegistry));
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> paginationForCarrier(Integer limit, Integer offset, Long carrierId) {
        List<Trip> trips = getJdbcTemplate()
                .query(ALL_TRIPS_PAGINATION,
                        new Object[]{carrierId, limit, offset},
                        new TripCRUDMapper(this.tripStateRegistry));
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> findByStatusForCarrier(Integer status, Long carrierId) {
        List<Trip> trips = getJdbcTemplate()
                .query(FIND_BY_STATUS,
                        new Object[]{carrierId, status},
                        new TripCRUDMapper(this.tripStateRegistry));
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> findByPlanetsForCarrier(String departurePlanet, String arrivalPlanet, Long carrierId) {
        List<Trip> trips;
        trips = getJdbcTemplate()
                .query(FIND_BY_PLANETS,
                        new Object[]{carrierId, departurePlanet, arrivalPlanet},
                        new TripCRUDMapper(this.tripStateRegistry));
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public void save(Trip trip) {
        getJdbcTemplate().update(INSERT_TRIP, trip.getCarrierId(), trip.getTripState(), trip.getTripPhoto(),
                trip.getDepartureSpaceport().getSpaceportId(), trip.getDepartureDate(),
                trip.getArrivalSpaceport().getSpaceportId(), trip.getArrivalDate(),
                trip.getCreationDate());
    }

    @Override
    public void update(Trip trip) {

    }

    private Optional<Trip> attachTicketClasses(Trip trip) {
        List<Long> rows = getJdbcTemplate().queryForList(FIND_ALL_TICKET_TRIPS, Long.class, trip.getTripId());
        trip.setTicketClasses(ticketClassDAO.findIn(rows));
        return Optional.of(trip);
    }

}
