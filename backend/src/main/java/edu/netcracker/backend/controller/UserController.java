package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.BoughtTicketDTO;
import edu.netcracker.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/bought-tickets")
    public BoughtTicketDTO buyTicket(BoughtTicketDTO boughtTicketDTO) {
        return userService.buyTicket(boughtTicketDTO);
    }
}
