package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.dao.mapper.TripMapper;
import edu.netcracker.backend.model.Trip;
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
    private final String findAllTicketTrips = "SELECT class_id FROM ticket_class WHERE trip_id = ?";

    private final UserDAO userDAO;
    private final TripReplyDAO tripReplyDAO;
    private final TripMapper tripMapper;

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

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO,
                       UserDAO userDAO,
                       TripReplyDAO tripReplyDAO,
                       TripMapper tripMapper) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
        this.tripReplyDAO = tripReplyDAO;
        this.tripMapper = tripMapper;
    }

    @Override
    public Optional<Trip> find(Number id) {
        try {
            Trip trip = getJdbcTemplate().queryForObject(SELECT_FULL, new Object[]{id}, tripMapper);

            if (trip != null) {
                attachTicketClassed(trip);
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

    private List<Trip> findAndAttach(String sql, Object[] data) {
        List<Trip> trips = getJdbcTemplate().query(sql, data, tripMapper);
        trips.forEach(this::attachTicketClassed);
        trips.forEach(this::attachReplies);
        return trips;
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


    protected void update(Trip trip) {
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

    private Trip attachTicketClassed(Trip trip) {
        List<Long> rows = getJdbcTemplate().queryForList(findAllTicketTrips, Long.class, trip.getTripId());
        trip.setTicketClasses(ticketClassDAO.findIn(rows));
        return trip;
    }

    private Trip attachReplies(Trip trip) {
        trip.setReplies(getJdbcTemplate().query(SELECT_REPLIES_BY_TRIP,
                                                new Object[]{trip.getTripId()},
                                                tripReplyDAO.getGenericMapper()));
        return trip;
    }
}