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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@PropertySource("classpath:sql/tripdao.properties")
public class TripDAOImpl extends CrudDAOImpl<Trip> implements TripDAO {

    private TicketClassDAO ticketClassDAO;

    private final UserDAO userDAO;

    private final TripMapper tripMapper;

    private final TripReplyMapper tripReplyMapper;

    @Value("${FIND_ALL_TICKET_TRIPS}")
    private String FIND_ALL_TICKET_TRIPS;

    @Value("${FIND_BY_CARRIER_ID}")
    private String FIND_BY_CARRIER_ID;

    @Value("${FIND_ALL}")
    private String FIND_ALL;

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

    @Value("${ALL_TRIPS_FOR_CARRIER}")
    private String ALL_TRIPS_FOR_CARRIER;

    @Value("${INSERT_TRIP}")
    private String INSERT_TRIP;

    @Value("${UPDATE_TRIP_INFO}")
    private String UPDATE_TRIP_INFO;

    @Value("${GET_ALL_TRIPS_WITH_ARRIVAL_AND_DEPARTURE_DATE_BELONG_TO_CARRIER}")
    private String GET_ALL_TRIPS_WITH_ARRIVAL_AND_DEPARTURE_DATE_BELONG_TO_CARRIER;

    @Value("${ALL_TRIPS_FOR_CARRIER_PAGINATION}")
    private String ALL_TRIPS_FOR_CARRIER_PAGINATION;

    @Value("${FIND_ALL_TRIPS}")
    private String FIND_ALL_TRIPS;

    private String PAGINATION = " LIMIT ? OFFSET ?";

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO,
                       UserDAO userDAO,
                       TripMapper tripMapper,
                       TripReplyMapper tripReplyMapper) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
        this.tripMapper = tripMapper;
        this.tripReplyMapper = tripReplyMapper;
    }

    /**
     * Method for selecting all trips which belong to specified carrier
     *
     * @param carrierId - id of carrier
     * @return list of trips ordered by creation date with attached ticket classes
     */
    @Override
    public List<Trip> allCarriersTrips(Long carrierId) {
        log.debug("Getting all trips for carrier");
        List<Trip> trips = getJdbcTemplate().query(ALL_TRIPS_FOR_CARRIER,
                                                   new Object[]{carrierId},
                                                   new TripCRUDMapper());
        log.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        log.debug("Attaching replies to trip");
        trips.forEach(trip -> this.attachReplies(trip));
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
        log.debug("Getting {} trips starting from {} ", limit, offset);
        List<Trip> trips = getJdbcTemplate().query(ALL_TRIPS_FOR_CARRIER_PAGINATION,
                                                   new Object[]{carrierId, limit, offset},
                                                   new TripCRUDMapper());
        log.debug("Attaching ticket classes to all trips");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId())));
        log.debug("Attaching replies to all trips");
        trips.forEach(trip -> this.attachReplies(trip));
        return trips;
    }

    /**
     * Method for adding new trips to database
     *
     * @param trip - trip to be added
     */
    public void add(Trip trip) {
        log.debug("Inserting new trip to database");
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
        log.debug("Updating info about trip with id {}", trip.getTripId());
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
               .append(" WHERE trip_status = 4 ");

        if (departurePlanet != null) {
            log.debug("Getting all from planet {}");
            objects.add(departurePlanet.toLowerCase());
            builder.append("AND LOWER(dp.planet_name) = ? ");
        }

        if (departureSpaceport != null) {
            log.debug("Getting all from port {}");
            objects.add(departureSpaceport.toLowerCase());
            builder.append("AND LOWER(ds.spaceport_name) = ? ");
        }

        if (arrivalPlanet != null) {
            log.debug("Getting all to planet {}");
            objects.add(arrivalPlanet.toLowerCase());
            builder.append("AND LOWER(arp.planet_name) = ? ");
        }

        if (arrivalSpaceport != null) {
            log.debug("Getting all to port {}");
            objects.add(arrivalSpaceport.toLowerCase());
            builder.append("AND LOWER(ars.spaceport_name) = ? ");
        }

        if (departureDate != null) {
            log.debug("Getting all ON {}");
            objects.add(departureDate);
            builder.append("AND TO_CHAR(departure_date, 'YYYY-MM-DD') = ? ");
        }

        builder.append(PAGINATION);

        objects.add(limit);
        objects.add(offset);

        trips.addAll(getJdbcTemplate().query(builder.toString(),
                                             objects.toArray(),
                                             new TripCRUDMapper()));

        log.debug("Attaching ticket classes to trip");
        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findByTripIdWithDiscount(trip.getTripId())));

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

        trips.addAll(getJdbcTemplate().query(FIND_BY_CARRIER_ID, new Object[]{id}, getGenericMapper()));

        return trips.stream()
                    .map(trip -> attachTicketClassed(trip).orElse(null))
                    .collect(Collectors.toList());
    }

    @Override
    public List<Trip> findAll() {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(FIND_ALL, getGenericMapper()));

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
