package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Discount;

import java.util.Optional;

public interface DiscountDAO {

    Optional<Discount> find(Number id);

    void save(Discount entity);

    void delete(Discount entity);

    void createDiscountForPossibleService(int possibleServiceId, Discount discount);

    void createDiscountForTicketClass(int ticketClassId, Discount discount);

}
