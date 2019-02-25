package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.TicketClass;

import java.util.Optional;

public interface TicketClassDAO extends CrudDAO<TicketClass> {
    void save(TicketClass ticketClass);

    Optional<TicketClass> find(Number id);

    void delete(TicketClass ticketClass);
}
