package edu.netcracker.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String forAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return "Admin " + userDetails.getUsername();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String forUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return "User " + userDetails.getUsername();
    }

    @GetMapping("/all/ok")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String forAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return "All " + userDetails.getUsername();
    }
}
