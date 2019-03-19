package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Ticket;
import edu.netcracker.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    void save(Ticket ticket);

    Optional<Ticket> find(Number id);

    void delete(Ticket ticket);

    List<Ticket> findAllByClass(Number id);

    void buyTicket(Ticket ticket, User user);

    Integer getRemainingSeatsForClass(Long classId);
}
