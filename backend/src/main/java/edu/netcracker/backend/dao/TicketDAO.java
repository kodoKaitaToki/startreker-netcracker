package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.model.Ticket;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketDAO {

    void save(Ticket ticket);

    Optional<Ticket> find(Number id);

    void delete(Ticket ticket);

    void createEmptyTicketForTicketClass(Long classId, Long seat);

    Integer getRemainingSeatsForClass(Long classId);

    List<Ticket> findAllByClass(Number id);

    void deleteAllTicketsOfTicketClass(Long id);

    List<HistoryTicket> findAllPurchasedByUser(Number user_id,
                                               Number limit,
                                               Number offset,
                                               LocalDate startDate,
                                               LocalDate endDate);
}
