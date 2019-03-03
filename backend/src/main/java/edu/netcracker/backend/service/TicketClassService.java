package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.DiscountTicketClassDTO;

import java.util.List;

public interface TicketClassService {

    List<DiscountTicketClassDTO> getTicketClassesRelatedToCarrier(Number userId);

    DiscountTicketClassDTO createDiscountForTicketClass(DiscountTicketClassDTO ticketClassDTO);

    DiscountTicketClassDTO deleteDiscountForTicketClass(Number discountId, Number userId);

}
