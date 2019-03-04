package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.dao.mapper.TripMapper;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.TripStateUtils;
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

    @Value("${SELECT_FULL}")
    private String SELECT_FULL;

    @Value("${UPDATE_FULL}")
    private String UPDATE_FULL;

    @Value("${CREATE_FULL}")
    private String CREATE_FULL;

    @Value("${ROW_EXISTS}")
    private String ROW_EXISTS;

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO, UserDAO userDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Optional<Trip> find(Number id) {
        try {
            Trip trip = getJdbcTemplate().queryForObject(
                    SELECT_FULL,
                    new Object[]{id},
                    new TripMapper()
            );

            if (trip != null) {
                attachTicketClassed(trip);
                // this doesn't look right...
                if (trip.getApprover() != null) userDAO.attachRoles(trip.getApprover());
                if (trip.getOwner() != null) userDAO.attachRoles(trip.getOwner());
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
        if(trip.getTripId() == null) return false;
        SqlRowSet rowSet = getJdbcTemplate().queryForRowSet(
                ROW_EXISTS,
                trip.getTripId()
        );
        return rowSet.next();
    }


    protected void update(Trip trip) {
        getJdbcTemplate().update(
                UPDATE_FULL,
                trip.getCreationDate(),
                trip.getDepartureDate(),
                trip.getArrivalDate(),
                trip.getTripState().getValue(),
                trip.getOwner().getUserId(),
                (trip.getApprover() == null ? null : trip.getApprover().getUserId()),
                trip.getTripPhoto(),
                trip.getTripId()
        );
    }

    private void create(Trip trip) {
        getJdbcTemplate().update(
                CREATE_FULL,
                getTripArguments(trip)
        );
    }

    private Object[] getTripArguments(Trip trip) {
        return new Object[]{
                trip.getCreationDate(),
                trip.getDepartureDate(),
                trip.getArrivalDate(),
                trip.getTripState().getValue(),
                trip.getOwner().getUserId(),
                (trip.getApprover() == null ? null : trip.getApprover().getUserId()),
                trip.getTripPhoto()
        };
    }

    private Trip attachTicketClassed(Trip trip) {
        List<Long> rows = getJdbcTemplate().queryForList(findAllTicketTrips, Long.class, trip.getTripId());
        trip.setTicketClasses(ticketClassDAO.findIn(rows));
        return trip;
    }

}
