package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.dao.mapper.TripCRUDMapper;
import edu.netcracker.backend.dao.mapper.TripMapper;
import edu.netcracker.backend.dao.mapper.TripReplyMapper;
import edu.netcracker.backend.dao.mapper.TripWithArrivalAndDepartureDataMapper;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:sql/tripdao.properties")
public class TripDAOImpl extends CrudDAOImpl<Trip> implements TripDAO {

    private TicketClassDAO ticketClassDAO;

    private final String FIND_ALL_TICKET_TRIPS = "SELECT class_id FROM ticket_class WHERE trip_id = ?";

    private final UserDAO userDAO;

    private final TripMapper tripMapper;

    private TripStateRegistry tripStateRegistry;

    private final TripReplyMapper tripReplyMapper;

    private final String findAllByCarrierId = "SELECT * FROM trip WHERE carrier_id = ?";

    private final String findAll = "SELECT * FROM trip";

    @Value("${SELECT_FULL}")
    private String SELECT_FULL;

    @Value("${SELECT_REPLIES_BY_TRIP}")
    private String SELECT_REPLIES_BY_TRIP;

    @Value("${UPDATE_FULL}")
    private String UPDATE_FULL;

    @Value("${CREATE_FULL}")
    private String CREATE_FULL;

    @Value("${ROW_EXISTS}")
    private String ROW_EXISTS;

    @Value("${SELECT_BY_CARRIER_BY_STATUS}")
    private String SELECT_BY_CARRIER_BY_STATUS;

    @Value("${SELECT_BY_CARRIER}")
    private String SELECT_BY_CARRIER;

    @Value("${SELECT_BY_APPROVER_BY_STATUS}")
    private String SELECT_BY_APPROVER_BY_STATUS;

    @Value("${SELECT_BY_STATUS}")
    private String SELECT_BY_STATUS;

    private static final String GET_ALL_TRIPS_WITH_ARRIVAL_AND_DEPARTURE_DATE_BELONG_TO_CARRIER = "SELECT "
                                                                                                  + "  trip_id, "
                                                                                                  + "  arrival_date, "
                                                                                                  + "  departure_date, "
                                                                                                  + "  arrival_sp.spaceport_name AS arrival_spaceport_name, "
                                                                                                  + "  departure_sp.spaceport_name AS departure_spaceport_name, "
                                                                                                  + "  arrival_p.planet_name AS arrival_planet_name, "
                                                                                                  + "  departure_p.planet_name AS departure_planet_name "
                                                                                                  + "FROM trip "
                                                                                                  + "INNER JOIN spaceport arrival_sp ON  trip.arrival_id = arrival_sp.spaceport_id "
                                                                                                  + "INNER JOIN spaceport departure_sp ON  trip.departure_id = departure_sp.spaceport_id "
                                                                                                  + "INNER JOIN planet arrival_p on arrival_p.planet_id = arrival_sp.planet_id "
                                                                                                  + "INNER JOIN planet departure_p on departure_p.planet_id = departure_sp.planet_id "
                                                                                                  + "WHERE carrier_id = ? AND trip.trip_status = 4 "
                                                                                                  + "ORDER BY trip_id DESC";


    private String FIND_ALL_TRIPS = "SELECT trip_id, carrier_id, trip_status, "
                                    + "ds.spaceport_id AS departure_spaceport_id, ds.spaceport_name AS departure_spaceport_name, "
                                    + "dp.planet_id AS departure_spaceport_planet_id, dp.planet_name AS departure_spaceport_planet_name, departure_date, "
                                    + "ars.spaceport_id AS arrival_spaceport_id, ars.spaceport_name AS arrival_spaceport_name, "
                                    + "arp.planet_id AS arrival_spaceport_planet_id, arp.planet_name AS arrival_spaceport_planet_name, arrival_date, "
                                    + "trip_photo, approver_id, t.creation_date "
                                    + "FROM trip as t "
                                    + "INNER JOIN spaceport AS ds ON ds.spaceport_id = t.departure_id "
                                    + "INNER JOIN planet AS dp ON dp.planet_id = ds.planet_id "
                                    + "INNER JOIN spaceport AS ars ON ars.spaceport_id = t.arrival_id "
                                    + "INNER JOIN planet AS arp ON arp.planet_id = ars.planet_id ";


    private String FIND_ALL_TRIPS_FOR_CARRIER = FIND_ALL_TRIPS + "WHERE carrier_id = ? AND trip_status != 7 ";

    private String ORDERED = "ORDER BY trip_status, t.creation_date DESC ";

    private String PAGINATION = "LIMIT ? OFFSET ?";

    private String FIND_ALL_TRIPS_FOR_USER = FIND_ALL_TRIPS
                                             + "WHERE trip_status = 4 "
                                             + "AND LOWER(dp.planet_name) = ? AND LOWER(ds.spaceport_name) = ? "
                                             + "AND LOWER(arp.planet_name) = ? AND LOWER(ars.spaceport_name) = ? "
                                             + "AND TO_CHAR(departure_date, 'YYYY-MM-DD') = ? "
                                             + PAGINATION;

    private String ALL_TRIPS_ORDERED = FIND_ALL_TRIPS_FOR_CARRIER + ORDERED;

    private String ALL_TRIPS_PAGINATION = ALL_TRIPS_ORDERED + PAGINATION;

    private String FIND_BY_STATUS = FIND_ALL_TRIPS_FOR_CARRIER + "AND trip_status = ? ";

    private String FIND_BY_STATUS_PAGINATION = FIND_BY_STATUS + PAGINATION;

    private String FIND_BY_PLANETS =
            FIND_ALL_TRIPS_FOR_CARRIER + "AND dp.planet_name = UPPER(?) " + "AND arp.planet_name = UPPER(?) ";

    private String INSERT_TRIP = "INSERT INTO TRIP (carrier_id, trip_status, trip_photo, departure_id, departure_date, "
                                 + "arrival_id, arrival_date, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private String UPDATE_TRIP_INFO =
            "UPDATE trip SET departure_id = ?, arrival_id = ?, departure_date = ?, arrival_date = ? WHERE trip_id = ?";


    private final Logger logger = LoggerFactory.getLogger(TripDAOImpl.class);

    private String PAGINATION = "LIMIT ? OFFSET ?";

    private String FIND_ALL_TRIPS = "SELECT trip_id, carrier_id, trip_status, "
                                    + "ds.spaceport_id AS departure_spaceport_id, ds.spaceport_name AS departure_spaceport_name, "
                                    + "dp.planet_id AS departure_planet_id, dp.planet_name AS departure_planet_name, departure_date, "
                                    + "ars.spaceport_id AS arrival_spaceport_id, ars.spaceport_name AS arrival_spaceport_name, "
                                    + "arp.planet_id AS arrival_planet_id, arp.planet_name AS arrival_planet_name, arrival_date, "
                                    + "trip_photo, approver_id, t.creation_date "
                                    + "FROM trip as t "
                                    + "INNER JOIN spaceport AS ds ON ds.spaceport_id = t.departure_id "
                                    + "INNER JOIN planet AS dp ON dp.planet_id = ds.planet_id "
                                    + "INNER JOIN spaceport AS ars ON ars.spaceport_id = t.arrival_id "
                                    + "INNER JOIN planet AS arp ON arp.planet_id = ars.planet_id ";

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO,
                       UserDAO userDAO,
                       TripMapper tripMapper,
                       TripReplyMapper tripReplyMapper,
                       TripStateRegistry tripStateRegistry) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
        this.tripMapper = tripMapper;
        this.tripReplyMapper = tripReplyMapper;
        this.tripStateRegistry = tripStateRegistry;

    }

    /**
     * Method for selecting all trips which belong to specified carrier
     *
     * @param carrierId - id of carrier
     * @return list of trips ordered by creation date with attached ticket classes
     */
    @Override
    public List<Trip> allCarriersTrips(Long carrierId) {
        logger.debug("Getting all trips for carrier");
        List<Trip> trips = getJdbcTemplate().query(ALL_TRIPS_ORDERED,
                                                   new Object[]{carrierId},
                                                   new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }


    /**
     * Method for selecting all trips which belong to specified carrier with pagination
     *
     * @param carrierId - id of carrier
     * @param limit     - amount of trips which should be returned
     * @param offset    - specifies from which number query should begin
     * @return list of trips ordered by creation date with attached ticket classes
     */

    @Override
    public List<Trip> paginationForCarrier(Integer limit, Integer offset, Long carrierId) {
        List<Trip> trips = getJdbcTemplate().query(ALL_TRIPS_PAGINATION,
                                                   new Object[]{carrierId, limit, offset},
                                                   new TripCRUDMapper(this.tripStateRegistry));
        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    /**
     * Method for selecting trips with specified status which belong to specified carrier
     *
     * @param carrierId - id of carrier
     * @param status    - status of trip
     * @return list of trips ordered by creation date with attached ticket classes
     */

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

    /**
     * Method for selecting trips with specified status which belong to specified carrier with pagination
     *
     * @param carrierId - id of carrier
     * @param status    - status of trip
     * @param limit     - amount of trips which should be returned
     * @param offset    - specifies from which number query should begin
     * @return list of trips ordered by creation date with attached ticket classes
     */

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

    /**
     * Method for selecting trips with specified status which belong to specified carrier with pagination
     *
     * @param carrierId       - id of carrier
     * @param departurePlanet - planet where from trip starts
     * @param arrivalPlanet   - planet of destination
     * @return list of trips ordered by creation date with attached ticket classes
     */

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
    public List<Trip> getAllTripsForUser(String departurePlanet,
                                         String departureSpaceport,
                                         String departureDate,
                                         String arrivalPlanet,
                                         String arrivalSpaceport,
                                         Integer limit,
                                         Integer offset) {
        logger.debug("Getting all trips from {} ({}) to {} ({}) ON {}",
                     departureSpaceport,
                     departurePlanet,
                     arrivalSpaceport,
                     arrivalPlanet,
                     departureDate);
        List<Trip> trips;
        trips = getJdbcTemplate().query(FIND_ALL_TRIPS_FOR_USER,
                                        new Object[]{departurePlanet.toLowerCase(),
                                                     departureSpaceport.toLowerCase(),
                                                     arrivalPlanet.toLowerCase(),
                                                     arrivalSpaceport.toLowerCase(),
                                                     departureDate,
                                                     limit,
                                                     offset},
                                        new TripCRUDMapper(this.tripStateRegistry));

        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        return trips;
    }

    /**
     * Method for adding new trips to database
     *
     * @param trip - trip to be added
     */

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

    /**
     * Method for updating trips in database
     *
     * @param trip - trip to be updated
     */
    @Override
    public void updateTripInfo(Trip trip) {
        logger.debug("Updating info about trip with id {}", trip.getTripId());
        getJdbcTemplate().update(UPDATE_TRIP_INFO,
                                 trip.getDepartureSpaceport()
                                     .getSpaceportId(),
                                 trip.getArrivalSpaceport()
                                     .getSpaceportId(),
                                 trip.getDepartureDate(),
                                 trip.getArrivalDate(),
                                 trip.getTripId());
    }

    @Override
    public Optional<Trip> find(Number id) {
        try {
            Trip trip = getJdbcTemplate().queryForObject(SELECT_FULL, new Object[]{id}, tripMapper);

            if (trip != null) {
                attachTicketClassed(trip);
                attachReplies(trip);
                return Optional.of(trip);
            }
        } catch (EmptyResultDataAccessException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public List<Trip> findAllByCarrierAndStatus(Integer userId, Integer status, Long offset, Long limit) {
        List<Trip> trips = getJdbcTemplate().query(SELECT_BY_CARRIER_BY_STATUS,
                                                   new Object[]{userId, status, offset, limit},
                                                   tripMapper);
        trips.forEach(this::attachTicketClassed);
        trips.forEach(this::attachReplies);
        return trips;
    }

    @Override
    public List<Trip> findAllByCarrier(Integer userId, Integer ignoredStatus, Long offset, Long limit) {
        return findAndAttach(SELECT_BY_CARRIER, new Object[]{userId, ignoredStatus, offset, limit});
    }

    @Override
    public List<Trip> findAllByApproverByStatus(Integer userId, Integer status, Long offset, Long limit) {
        return findAndAttach(SELECT_BY_APPROVER_BY_STATUS, new Object[]{userId, status, offset, limit});
    }

    @Override
    public List<Trip> findAllByStatus(Integer status, Long offset, Long limit) {
        return findAndAttach(SELECT_BY_STATUS, new Object[]{status, offset, limit});
    }

    @Override
    public List<Trip> getAllTripsForUser(String departurePlanet,
                                         String departureSpaceport,
                                         String departureDate,
                                         String arrivalPlanet,
                                         String arrivalSpaceport,
                                         Integer limit,
                                         Integer offset) {
        List<Trip> trips = new ArrayList<>();

        List<Object> objects = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        builder.append(FIND_ALL_TRIPS)
               .append("WHERE trip_status = 4 ");

        if (departurePlanet != null) {
            logger.debug("Getting all from planet {}");
            objects.add(departurePlanet.toLowerCase());
            builder.append("AND LOWER(dp.planet_name) = ? ");
        }

        if (departureSpaceport != null) {
            logger.debug("Getting all from port {}");
            objects.add(departureSpaceport.toLowerCase());
            builder.append("AND LOWER(ds.spaceport_name) = ? ");
        }

        if (arrivalPlanet != null) {
            logger.debug("Getting all to planet {}");
            objects.add(arrivalPlanet.toLowerCase());
            builder.append("AND LOWER(arp.planet_name) = ? ");
        }

        if (arrivalSpaceport != null) {
            logger.debug("Getting all to port {}");
            objects.add(arrivalSpaceport.toLowerCase());
            builder.append("AND LOWER(ars.spaceport_name) = ? ");
        }

        if (departureDate != null) {
            logger.debug("Getting all ON {}");
            objects.add(departureDate);
            builder.append("AND TO_CHAR(departure_date, 'YYYY-MM-DD') = ? ");
        }

        builder.append(PAGINATION);

        objects.add(limit);
        objects.add(offset);

        trips.addAll(getJdbcTemplate().query(builder.toString(), objects.toArray(), new TripCRUDMapper()));

        logger.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));

        return trips;
    }

    private List<Trip> findAndAttach(String sql, Object[] data) {
        List<Trip> trips = getJdbcTemplate().query(sql, data, tripMapper);
        trips.forEach(this::attachTicketClassed);
        trips.forEach(this::attachReplies);
        return trips;
    }

    @Override
    public List<TripWithArrivalAndDepartureData> getAllTripsWitArrivalAndDepatureDataBelongToCarrier(Number carrierId) {
        return new ArrayList<>(getJdbcTemplate().query(GET_ALL_TRIPS_WITH_ARRIVAL_AND_DEPARTURE_DATE_BELONG_TO_CARRIER,
                                                       new Object[]{carrierId},
                                                       new TripWithArrivalAndDepartureDataMapper()));
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


    @Override
    public List<Trip> findByCarrierId(Number id) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(findAllByCarrierId, new Object[]{id}, getGenericMapper()));

        return trips.stream()
                    .map(trip -> attachTicketClassed(trip).orElse(null))
                    .collect(Collectors.toList());
    }

    @Override
    public List<Trip> findAll() {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(findAll, getGenericMapper()));

        return trips.stream()
                    .map(trip -> attachTicketClassed(trip).orElse(null))
                    .collect(Collectors.toList());
    }

    private Optional<Trip> attachTicketClassed(Trip trip) {
        if (trip == null) {
            return Optional.empty();
        }

        List<Long> rows = getJdbcTemplate().queryForList(FIND_ALL_TICKET_TRIPS, Long.class, trip.getTripId());
        trip.setTicketClasses(ticketClassDAO.findIn(rows));

        return Optional.of(trip);
    }

    private Trip attachReplies(Trip trip) {
        trip.setReplies(getJdbcTemplate().query(SELECT_REPLIES_BY_TRIP,
                                                new Object[]{trip.getTripId()},
                                                tripReplyMapper));

        return trip;
    }
}
