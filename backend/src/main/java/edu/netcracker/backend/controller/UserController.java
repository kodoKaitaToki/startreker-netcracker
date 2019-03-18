package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.BoughtTicketDTO;
import edu.netcracker.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/user/bought-tickets")
    public BoughtTicketDTO buyTicket(@Valid @RequestBody BoughtTicketDTO boughtTicketDTO) {
        return userService.buyTicket(boughtTicketDTO);
    }
}
