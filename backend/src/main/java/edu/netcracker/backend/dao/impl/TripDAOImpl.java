package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.dao.mapper.TripMapper;
import edu.netcracker.backend.dao.mapper.TripWithArrivalAndDepartureDataMapper;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sql/tripdao.properties")
public class TripDAOImpl extends CrudDAOImpl<Trip> implements TripDAO {

    private TicketClassDAO ticketClassDAO;
    private final String findAllTicketTrips = "SELECT class_id FROM ticket_class WHERE trip_id = ?";

    private final UserDAO userDAO;
    private final TripMapper tripMapper;

    @Value("${SELECT_FULL}")
    private String SELECT_FULL;

    @Value("${UPDATE_FULL}")
    private String UPDATE_FULL;

    @Value("${CREATE_FULL}")
    private String CREATE_FULL;

    @Value("${ROW_EXISTS}")
    private String ROW_EXISTS;

    private static final String GET_ALL_TRIPS_WITH_ARRIVAL_AND_DEPARTURE_DATE_BELONG_TO_CARRIER = "SELECT " +
            "  trip_id, " +
            "  arrival_date, " +
            "  departure_date, " +
            "  arrival_sp.spaceport_name AS arrival_spaceport_name, " +
            "  departure_sp.spaceport_name AS departure_spaceport_name, " +
            "  arrival_p.planet_name AS arrival_planet_name, " +
            "  departure_p.planet_name AS departure_planet_name " +
            "FROM trip " +
            "INNER JOIN spaceport arrival_sp ON  trip.arrival_id = arrival_sp.spaceport_id " +
            "INNER JOIN spaceport departure_sp ON  trip.departure_id = departure_sp.spaceport_id " +
            "INNER JOIN planet arrival_p on arrival_p.planet_id = arrival_sp.planet_id " +
            "INNER JOIN planet departure_p on departure_p.planet_id = departure_sp.planet_id " +
            "WHERE carrier_id = ? " +
            "ORDER BY trip_id DESC";

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO, UserDAO userDAO, TripMapper tripMapper) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
        this.tripMapper = tripMapper;
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
    public List<TripWithArrivalAndDepartureData> getAllTripsWitArrivalAndDepatureDataBelongToCarrier(Number carrierId) {
        return new ArrayList<>(getJdbcTemplate().query(
                GET_ALL_TRIPS_WITH_ARRIVAL_AND_DEPARTURE_DATE_BELONG_TO_CARRIER,
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
                trip.getTripId()
        );
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

}
