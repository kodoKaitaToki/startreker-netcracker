package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.dao.mapper.TripCRUDMapper;
import edu.netcracker.backend.dao.mapper.TripMapper;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sql/tripdao.properties")
public class TripDAOImpl extends CrudDAOImpl<Trip> implements TripDAO {

    private TicketClassDAO ticketClassDAO;
    private final String FIND_ALL_TICKET_TRIPS = "SELECT class_id FROM ticket_class WHERE trip_id = ?";

    private final UserDAO userDAO;
    private final TripMapper tripMapper;
    private TripStateRegistry tripStateRegistry;

    private final Logger logger = LoggerFactory.getLogger(BundleDAOImpl.class);

    @Value("${SELECT_FULL}")
    private String SELECT_FULL;

    @Value("${UPDATE_FULL}")
    private String UPDATE_FULL;

    @Value("${CREATE_FULL}")
    private String CREATE_FULL;

    @Value("${ROW_EXISTS}")
    private String ROW_EXISTS;

    private String FIND_ALL_TRIPS_FOR_CARRIER = "SELECT trip_id, carrier_id, trip_status, "
                                                + "ds.spaceport_id AS departure_spaceport_id, ds.spaceport_name AS departure_spaceport_name, "
                                                + "dp.planet_id AS departure_planet_id, dp.planet_name AS departure_planet_name, departure_date, "
                                                + "ars.spaceport_id AS arrival_spaceport_id, ars.spaceport_name AS arrival_spaceport_name, "
                                                + "arp.planet_id AS arrival_planet_id, arp.planet_name AS arrival_planet_name, arrival_date, "
                                                + "trip_photo, approver_id, t.creation_date "
                                                + "FROM trip as t "
                                                + "INNER JOIN spaceport AS ds ON ds.spaceport_id = t.departure_id "
                                                + "INNER JOIN planet AS dp ON dp.planet_id = ds.planet_id "
                                                + "INNER JOIN spaceport AS ars ON ars.spaceport_id = t.arrival_id "
                                                + "INNER JOIN planet AS arp ON arp.planet_id = ars.planet_id "
                                                + "WHERE carrier_id = ? AND trip_status != 7 ";

    private String PAGINATION = "ORDER BY t.creation_date DESC LIMIT ? OFFSET ?";

    private String ALL_TRIPS_PAGINATION = FIND_ALL_TRIPS_FOR_CARRIER + PAGINATION;

    private String FIND_BY_STATUS = FIND_ALL_TRIPS_FOR_CARRIER + "AND trip_status = ? ";

    private String FIND_BY_STATUS_PAGINATION = FIND_BY_STATUS + PAGINATION;

    private String FIND_BY_PLANETS =
            FIND_ALL_TRIPS_FOR_CARRIER + "AND dp.planet_name = UPPER(?) " + "AND arp.planet_name = UPPER(?) ";

    private String INSERT_TRIP = "INSERT INTO TRIP (carrier_id, trip_status, trip_photo, departure_id, departure_date, "
                                 + "arrival_id, arrival_date, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO,
                       UserDAO userDAO,
                       TripMapper tripMapper,
                       TripStateRegistry tripStateRegistry) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
        this.tripMapper = tripMapper;
        this.tripStateRegistry = tripStateRegistry;
    }

    @Override
    public List<Trip> allCarriersTrips(Long carrierId) {
        logger.debug("Getting all trips for carrier");
        List<Trip> trips = getJdbcTemplate().query(FIND_ALL_TRIPS_FOR_CARRIER,
                                                   new Object[]{carrierId},
                                                   new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> paginationForCarrier(Integer limit, Integer offset, Long carrierId) {
        List<Trip> trips = getJdbcTemplate().query(ALL_TRIPS_PAGINATION,
                                                   new Object[]{carrierId, limit, offset},
                                                   new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> findByStatusForCarrier(Integer status, Long carrierId) {
        logger.debug("Getting trips for carrier filtered by status {}", status);
        List<Trip> trips = getJdbcTemplate().query(FIND_BY_STATUS,
                                                   new Object[]{carrierId, status},
                                                   new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> findByStatusForCarrierPagination(Integer status, Long carrierId, Integer limit, Integer offset) {
        logger.debug("Getting {} trips for carrier filtered by status {} with pagination from {} ",
                     limit,
                     status,
                     offset);
        List<Trip> trips = getJdbcTemplate().query(FIND_BY_STATUS_PAGINATION,
                                                   new Object[]{carrierId, status, limit, offset},
                                                   new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public List<Trip> findByPlanetsForCarrier(String departurePlanet, String arrivalPlanet, Long carrierId) {
        logger.debug("Getting all trips from {} to {}", departurePlanet, arrivalPlanet);
        List<Trip> trips;
        trips = getJdbcTemplate().query(FIND_BY_PLANETS,
                                        new Object[]{carrierId, departurePlanet, arrivalPlanet},
                                        new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    @Override
    public Optional<Trip> find(Number id) {
        try {
            Trip trip = getJdbcTemplate().queryForObject(SELECT_FULL, new Object[]{id}, tripMapper);

            if (trip != null) {
                attachTicketClassed(trip);
                // this doesn't look right...
                if (trip.getApprover() != null) {
                    userDAO.attachRoles(trip.getApprover());
                }
                if (trip.getOwner() != null) {
                    userDAO.attachRoles(trip.getOwner());
                }
                return Optional.of(trip);
            }
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void save(Trip trip) {
        if (exists(trip)) {
            update(trip);
        } else {
            create(trip);
        }
    }

    protected boolean exists(Trip trip) {
        if (trip.getTripId() == null) {
            return false;
        }
        SqlRowSet rowSet = getJdbcTemplate().queryForRowSet(ROW_EXISTS, trip.getTripId());
        return rowSet.next();
    }


    public void update(Trip trip) {
        getJdbcTemplate().update(UPDATE_FULL,
                                 trip.getCreationDate(),
                                 trip.getDepartureDate(),
                                 trip.getArrivalDate(),
                                 trip.getTripState()
                                     .getDatabaseValue(),
                                 trip.getOwner()
                                     .getUserId(),
                                 (trip.getApprover() == null
                                         ? null
                                         : trip.getApprover()
                                               .getUserId()),
                                 trip.getTripPhoto(),
                                 trip.getTripId());
    }

    public void add(Trip trip) {
        logger.debug("Inserting new trip to database");
        getJdbcTemplate().update(INSERT_TRIP,
                                 trip.getOwner()
                                     .getUserId(),
                                 trip.getTripState()
                                     .getDatabaseValue(),
                                 trip.getTripPhoto(),
                                 trip.getDepartureSpaceport()
                                     .getSpaceportId(),
                                 trip.getDepartureDate(),
                                 trip.getArrivalSpaceport()
                                     .getSpaceportId(),
                                 trip.getArrivalDate(),
                                 trip.getCreationDate());
    }

    private void create(Trip trip) {
        getJdbcTemplate().update(CREATE_FULL, getTripArguments(trip));
    }

    private Object[] getTripArguments(Trip trip) {
        return new Object[]{trip.getCreationDate(),
                            trip.getDepartureDate(),
                            trip.getArrivalDate(),
                            trip.getTripState().getDatabaseValue(),
                            trip.getOwner().getUserId(),
                            (trip.getApprover() == null
                                    ? null
                                    : trip.getApprover()
                                          .getUserId()),
                            trip.getTripPhoto()};
    }

    private Trip attachTicketClassed(Trip trip) {
        List<Long> rows = getJdbcTemplate().queryForList(FIND_ALL_TICKET_TRIPS, Long.class, trip.getTripId());
        trip.setTicketClasses(ticketClassDAO.findIn(rows));
        return trip;
    }

}
