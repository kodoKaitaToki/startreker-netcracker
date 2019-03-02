package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.TicketClassDTO;

import java.util.List;

public interface TicketClassService {

    List<TicketClassDTO> getTicketClassesRelatedToCarrier(Number userId);

    TicketClassDTO createDiscountForTicketClass(TicketClassDTO ticketClassDTO);

    TicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId);

}
