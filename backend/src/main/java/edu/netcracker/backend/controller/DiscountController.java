package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.DiscountSuggestionDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataDTO;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.TripService;
import edu.netcracker.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DiscountController {

    private final TicketClassService ticketClassService;

    private final SuggestionService suggestionService;

    private final TripService tripService;

    private final SecurityContext securityContext;

    @Autowired
    public DiscountController(TicketClassService ticketClassService,
                              SuggestionService suggestionService,
                              TripService tripService,
                              SecurityContext securityContext) {
        this.ticketClassService = ticketClassService;
        this.suggestionService = suggestionService;
        this.tripService = tripService;
        this.securityContext = securityContext;
    }

    @GetMapping("/api/v1/class-ticket/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public List<TripWithArrivalAndDepartureDataDTO> getClassTickets() {
        return tripService.getAllTripsWithTicketClassAndDiscountsBelongToCarrier(securityContext.getUser()
                                                                                                .getUserId());
    }

    @PostMapping("/api/v1/class-ticket/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountTicketClassDTO createDiscountForClassTicket(@Valid @RequestBody
                                                                       DiscountTicketClassDTO ticketClassDTO) {
        return ticketClassService.createDiscountForTicketClass(ticketClassDTO,
                                                               securityContext.getUser()
                                                                              .getUserId());
    }

    @DeleteMapping("/api/v1/class-ticket/discount/{discountId}")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountTicketClassDTO deleteDiscountForClassTicket(@PathVariable("discountId") Number discountId) {
        return ticketClassService.deleteDiscountForTicketClass(discountId,
                                                               securityContext.getUser()
                                                                              .getUserId());
    }

    @GetMapping("/api/v1/suggestion/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public List<TripWithArrivalAndDepartureDataDTO> getSuggestions() {
        return tripService.getAllTripsWithSuggestionAndDiscountsBelongToCarrier(securityContext.getUser()
                                                                                               .getUserId());
    }

    @PostMapping("/api/v1/suggestion/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountSuggestionDTO createDiscountForSuggestion(@Valid @RequestBody
                                                                     DiscountSuggestionDTO simpleSuggestionDTO) {
        return suggestionService.createDiscountForSuggestion(simpleSuggestionDTO,
                                                             securityContext.getUser()
                                                                            .getUserId());
    }

    @DeleteMapping("/api/v1/suggestion/discount/{discountId}")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountSuggestionDTO deleteDiscountForSuggestion(@PathVariable("discountId") Number discountId) {
        return suggestionService.deleteDiscountForSuggestion(discountId,
                                                             securityContext.getUser()
                                                                            .getUserId());
    }
}
