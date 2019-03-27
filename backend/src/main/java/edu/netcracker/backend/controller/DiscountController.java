package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.DiscountSuggestionDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataDTO;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
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

    @GetMapping("/class-ticket/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public List<TripWithArrivalAndDepartureDataDTO> getClassTickets() {
        log.debug("DiscountController.getClassTickets() was invoked");
        return tripService.getAllTripsWithTicketClassAndDiscountsBelongToCarrier(securityContext.getUser()
                                                                                                .getUserId());
    }

    @PostMapping("/class-ticket/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountTicketClassDTO createDiscountForClassTicket(@Valid @RequestBody
                                                                       DiscountTicketClassDTO ticketClassDTO) {
        log.debug("DiscountController.createDiscountForClassTicket(DiscountTicketClassDTO ticketClassDTO) was invoked");
        return ticketClassService.createDiscountForTicketClass(ticketClassDTO,
                                                               securityContext.getUser()
                                                                              .getUserId());
    }

    @DeleteMapping("/class-ticket/discount/{discountId}")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountTicketClassDTO deleteDiscountForClassTicket(@PathVariable("discountId") Number discountId) {
        log.debug("DiscountController.deleteDiscountForClassTicket(Number discountId) was invoked");
        return ticketClassService.deleteDiscountForTicketClass(discountId,
                                                               securityContext.getUser()
                                                                              .getUserId());
    }

    @GetMapping("/suggestion/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public List<TripWithArrivalAndDepartureDataDTO> getSuggestions() {
        log.debug("DiscountController.getSuggestions() was invoked");
        return tripService.getAllTripsWithSuggestionAndDiscountsBelongToCarrier(securityContext.getUser()
                                                                                               .getUserId());
    }

    @PostMapping("/suggestion/discount")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountSuggestionDTO createDiscountForSuggestion(@Valid @RequestBody
                                                                     DiscountSuggestionDTO simpleSuggestionDTO) {
        log.debug(
                "DiscountController.createDiscountForSuggestion(DiscountSuggestionDTO simpleSuggestionDTO) was invoked");
        return suggestionService.createDiscountForSuggestion(simpleSuggestionDTO,
                                                             securityContext.getUser()
                                                                            .getUserId());
    }

    @DeleteMapping("/suggestion/discount/{discountId}")
    @PreAuthorize("hasRole('CARRIER')")
    public DiscountSuggestionDTO deleteDiscountForSuggestion(@PathVariable("discountId") Number discountId) {
        log.debug("DiscountController.deleteDiscountForSuggestion(Number discountId) was invoked");
        return suggestionService.deleteDiscountForSuggestion(discountId,
                                                             securityContext.getUser()
                                                                            .getUserId());
    }
}