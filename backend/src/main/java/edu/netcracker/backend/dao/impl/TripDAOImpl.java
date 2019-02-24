package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TripDAOImpl extends CrudDAO<Trip> implements TripDAO {
    private TicketClassDAO ticketClassDAO;
    private final String findAllTicketTrips = "SELECT class_id FROM ticket_class WHERE trip_id = ?";
    private final String findAllByCarrierId = "SELECT * FROM trip WHERE carrier_id = ?";
    private final String findAll = "SELECT * FROM trip";

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
    public List<Trip> findByCarrierId(Number id) {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findAllByCarrierId,
                new Object[]{id},
                getGenericMapper()));

        return trips.stream().map(trip -> attachTicketClassed(trip).orElse(null))
                .collect(Collectors.toList());
    }

    @Override
    public List<Trip> findAll() {
        List<Trip> trips = new ArrayList<>();

        trips.addAll(getJdbcTemplate().query(
                findAll,
                getGenericMapper()));

        return trips.stream().map(trip -> attachTicketClassed(trip).orElse(null))
                .collect(Collectors.toList());
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
