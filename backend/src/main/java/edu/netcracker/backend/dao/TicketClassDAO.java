package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.TicketClass;

import java.util.List;
import java.util.Optional;

public interface TicketClassDAO extends CrudDAO<TicketClass> {
    void save(TicketClass ticketClass);

    Optional<TicketClass> find(Number id);

    List<TicketClass> findByTripId(Number id);

    List<TicketClass> findTicketClassWithItemNumber(Number BundleId, Number TripId);

    void delete(TicketClass ticketClass);

    List<TicketClass> getAllTicketClassesRelatedToCarrier(Number carrierId);

    Optional<TicketClass> getTicketClassByDiscount(Number userId, Number discountId);

    void create(TicketClass ticketClass);

    void update(TicketClass ticketClass);

    void deleteDiscountsForTicketClasses(List<Long> collect);
}
