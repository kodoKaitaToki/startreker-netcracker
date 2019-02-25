package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.TicketClass;

import java.util.List;
import java.util.Optional;

public interface TicketClassDAO {
    void save(TicketClass ticketClass);

    Optional<TicketClass> find(Number id);

    List<TicketClass> findByTripId(Number id);

    void delete(TicketClass ticketClass);
}
