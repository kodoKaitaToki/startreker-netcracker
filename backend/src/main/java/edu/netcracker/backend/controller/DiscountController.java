package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.TicketClassDTO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DiscountController {

    private final TicketClassService ticketClassService;

    private final UserService userService;

    @Autowired
    public DiscountController(TicketClassService ticketClassService, UserService userService) {
        this.ticketClassService = ticketClassService;
        this.userService = userService;
    }

    @GetMapping("/api/v1/class-ticket/discount")
    public List<TicketClassDTO> getClassTickets(@RequestParam("username") String username) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return ticketClassService.getTicketClassesRelatedToCarrier(user.getUserId());
    }

    @PostMapping("/api/v1/class-ticket/discount")
    public TicketClassDTO createDiscountForClassTicket(@RequestParam("username") String username,
                                                       @Valid @RequestBody TicketClassDTO ticketClassDTO) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return ticketClassService.createDiscountForTicketClass(ticketClassDTO);
    }

    @DeleteMapping("/api/v1/class-ticket/discount/{discountId}")
    public TicketClassDTO deleteDiscountForClassTicket(@RequestParam("username") String username,
                                                       @PathVariable("discountId") Number discountId) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found ", HttpStatus.NOT_FOUND);
        }

        return ticketClassService.deleteDiscountForTicketClass(discountId, user.getUserId());
    }
}
