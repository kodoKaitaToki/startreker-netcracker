package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.model.TicketClass;

import java.util.List;
import java.util.Map;

public interface TicketClassService {

    TicketClass find(Number ticketClassId);

    List<DiscountTicketClassDTO> getTicketClassesRelatedToCarrier(Number userId);

    DiscountTicketClassDTO createDiscountForTicketClass(DiscountTicketClassDTO ticketClassDTO, Number userId);

    DiscountTicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId);

    Map<Long, List<TicketClass>> getAllTicketClassesBelongToTrips(List<Number> tripIds);

    void createNewTicketClass(TicketClass ticketClass);
}
