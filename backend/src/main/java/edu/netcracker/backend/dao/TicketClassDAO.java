package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.TicketClass;

import java.util.List;
import java.util.Optional;

public interface TicketClassDAO extends CrudDAO<TicketClass> {
    void save(TicketClass ticketClass);

    Optional<TicketClass> find(Number id);

    void delete(TicketClass ticketClass);

    List<TicketClass> getAllTicketClassesRelatedToCarrier(Number carrierId);

    Optional<TicketClass> getTicketClassByDiscount(Number userId, Number discountId);

    void deleteDiscountsForTicketClasses(List<Long> collect);
}
