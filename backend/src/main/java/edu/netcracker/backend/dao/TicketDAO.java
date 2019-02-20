package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    void save(Ticket ticket);

    Optional<Ticket> find(Number id);

    void delete(Ticket ticket);

    List<Ticket> findAllByClass(Number id);
}
