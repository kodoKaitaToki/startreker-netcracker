package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.ChangePasswordForm;
import edu.netcracker.backend.message.request.EmailFrom;
import edu.netcracker.backend.message.request.SignInForm;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.response.JwtResponse;
import edu.netcracker.backend.message.response.Message;
import edu.netcracker.backend.message.response.UserDTO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/api/auth/sign-up")
    public UserDTO signUp(@Valid @RequestBody SignUpForm signUpForm, HttpServletRequest request) {
        User user = authenticationService.signUp(signUpForm, request);

        return UserDTO.from(user);
    }

    @PostMapping(path = "/api/auth/password-recovery")
    public EmailFrom passwordRecovery(@Valid @RequestBody EmailFrom emailFrom) {
        authenticationService.passwordRecovery(emailFrom);

        return emailFrom;
    }

    @PostMapping(path = "/api/log-out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CARRIER') or hasRole('APPROVER') or hasRole('USER')")
    public ResponseEntity<Message> logOut() {
        return ResponseEntity.ok().body(authenticationService.logOut());
    }

    @PatchMapping(path = "/api/change-password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CARRIER') or hasRole('APPROVER') or hasRole('USER')")
    public ResponseEntity<Message> changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm) {
        return ResponseEntity.ok().body(authenticationService.changePassword(changePasswordForm));
    }

    @GetMapping(path = "api/auth/confirm-password")
    public ResponseEntity<Message> confirmPassword(@Valid @RequestParam("token") String token) {

        return ResponseEntity.ok().body(authenticationService.confirmPassword(token));
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody SignInForm signInForm) {
        return ResponseEntity.ok(authenticationService.signIn(signInForm));
    }
}
