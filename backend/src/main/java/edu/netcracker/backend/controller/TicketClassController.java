package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.trips.TicketClassCreation;
import edu.netcracker.backend.service.TicketClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class TicketClassController {

    private final TicketClassService ticketClassService;

    @Autowired
    public TicketClassController(TicketClassService ticketClassService) {
        this.ticketClassService = ticketClassService;
    }

    @PostMapping("api/v1/ticket-class")
    //    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public void createOrUpdateTicketClass(@RequestBody TicketClassCreation ticketClass) {
        this.ticketClassService.createOrUpdate(ticketClass.toTicketClass());
    }

    @DeleteMapping("api/v1/ticket-class/{id}")
    public void deleteTicketClass(@Valid @NotNull @PathVariable Long id) {
        this.ticketClassService.deleteTicketClass(id);
    }
}
