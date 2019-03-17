package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.trips.TicketClassCreation;
import edu.netcracker.backend.service.TicketClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketClassController {

    private final TicketClassService ticketClassService;

    @Autowired
    public TicketClassController(TicketClassService ticketClassService) {
        this.ticketClassService = ticketClassService;
    }

    @PostMapping("api/v1/ticket-class")
    //    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public void createTicketClass(@RequestBody TicketClassCreation ticketClass) {
        this.ticketClassService.createNewTicketClass(ticketClass.toTicketClass());
    }
//
//    @PutMapping("api/v1/ticket-class")
//    public void updateTicketClass() {
//
//    }
//
//    @DeleteMapping("api/v1/ticket-class")
//    public void deleteTicketClass() {
//
//    }
}
