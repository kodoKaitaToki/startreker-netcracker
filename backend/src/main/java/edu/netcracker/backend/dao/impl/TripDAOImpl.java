package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

    @Value("${findOwner.sql}")
    private String findOwnerSql;

    @Autowired
    public TripDAOImpl(TicketClassDAO ticketClassDAO, UserDAO userDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.userDAO = userDAO;
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
    public User findOwner(Trip trip) {
        return getJdbcTemplate().queryForObject(
                findOwnerSql,
                new Object[]{trip.getTripId()},
                userDAO.getGenericMapper()
        );
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
