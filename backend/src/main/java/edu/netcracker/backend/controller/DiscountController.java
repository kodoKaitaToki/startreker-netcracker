package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.DiscountSuggestionDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataDTO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.TripService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DiscountController {

    private final TicketClassService ticketClassService;

    private final UserService userService;

    private final SuggestionService suggestionService;

    private final TripService tripService;

    @Autowired
    public DiscountController(TicketClassService ticketClassService,
                              UserService userService,
                              SuggestionService suggestionService,
                              TripService tripService) {
        this.ticketClassService = ticketClassService;
        this.userService = userService;
        this.suggestionService = suggestionService;
        this.tripService = tripService;
    }

    @GetMapping("/api/v1/class-ticket/discount")
    public List<TripWithArrivalAndDepartureDataDTO> getClassTickets(@RequestParam("username") String username) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return tripService.getAllTripsWithTicketClassAndDiscountsBelongToCarrier(user.getUserId());
    }

    @PostMapping("/api/v1/class-ticket/discount")
    public DiscountTicketClassDTO createDiscountForClassTicket(@RequestParam("username") String username,
                                                               @Valid @RequestBody DiscountTicketClassDTO ticketClassDTO) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return ticketClassService.createDiscountForTicketClass(ticketClassDTO, user.getUserId());
    }

    @DeleteMapping("/api/v1/class-ticket/discount/{discountId}")
    public DiscountTicketClassDTO deleteDiscountForClassTicket(@RequestParam("username") String username,
                                                               @PathVariable("discountId") Number discountId) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return ticketClassService.deleteDiscountForTicketClass(discountId, user.getUserId());
    }

    @GetMapping("/api/v1/suggestion/discount")
    public List<TripWithArrivalAndDepartureDataDTO> getSuggestions(@RequestParam("username") String username) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return tripService.getAllTripsWithSuggestionAndDiscountsBelongToCarrier(user.getUserId());
    }

    @PostMapping("/api/v1/suggestion/discount")
    public DiscountSuggestionDTO createDiscountForSuggestion(@RequestParam("username") String username,
                                                             @Valid @RequestBody DiscountSuggestionDTO simpleSuggestionDTO) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return suggestionService.createDiscountForSuggestion(simpleSuggestionDTO, user.getUserId());
    }

    @DeleteMapping("/api/v1/suggestion/discount/{discountId}")
    public DiscountSuggestionDTO deleteDiscountForSuggestion(@RequestParam("username") String username,
                                                             @PathVariable("discountId") Number discountId) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return suggestionService.deleteDiscountForSuggestion(discountId, user.getUserId());
    }
}
