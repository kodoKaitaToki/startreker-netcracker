package edu.netcracker.backend.controller;

import edu.netcracker.backend.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @RequestMapping("/api")
    public User getUser() {

        // minor  changes

        return new User("Hello", "World");
    }
}
