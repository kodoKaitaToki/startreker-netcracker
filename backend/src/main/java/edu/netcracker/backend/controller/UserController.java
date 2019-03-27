package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.BoughtTicketDTO;
import edu.netcracker.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("bought-tickets")
    public BoughtTicketDTO buyTicket(@Valid @RequestBody BoughtTicketDTO boughtTicketDTO) {
        return userService.buyTicket(boughtTicketDTO);
    }
}
