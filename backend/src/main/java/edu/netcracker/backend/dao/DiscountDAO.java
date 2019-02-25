package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountDAO {

    Optional<Discount> find(Number id);

    void save(Discount entity);

    void createDiscountForPossibleService(int possibleServiceId, Discount discount);

    void createDiscountForTicketClass(int ticketClassId, Discount discount);

    void createDiscountForAllPossibleServicesForTicketClass(int ticketClassId, Discount discount);

    void createDiscountForAllTicketClassesForTrip(int tripId, Discount discount);

    Optional<Discount> getDiscountByPossibleServiceId(int possibleServiceId);

    Optional<Discount> getDiscountByTicketClassId(int ticketClassId);

    void deleteDiscountForPossibleService(int possibleServiceId);

    void deleteDiscountForTicketClass(int ticketClassId);

    void deleteDiscountsForAllTicketClassesForTrip(int tripId);

    void deleteDiscountsForAllPossibleServiceForClassTicket(int ticketClassId);
}
