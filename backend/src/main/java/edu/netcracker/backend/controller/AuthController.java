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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "sign-up")
    public UserDTO signUp(@Valid @RequestBody SignUpForm signUpForm, HttpServletRequest request) {
        log.debug("AuthController.signUp(SignUpForm signUpForm, HttpServletRequest request) was invoked");
        User user = authenticationService.signUp(signUpForm, request);
        return UserDTO.from(user);
    }

    @PostMapping(path = "password-recovery")
    public EmailFrom passwordRecovery(@Valid @RequestBody EmailFrom emailFrom) {
        log.debug("AuthController.passwordRecovery(EmailFrom emailFrom) was invoked");
        authenticationService.passwordRecovery(emailFrom);

        return emailFrom;
    }

    @PostMapping(path = "log-out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CARRIER') or hasRole('APPROVER') or hasRole('USER')")
    public ResponseEntity<Message> logOut() {
        log.debug("AuthController.logOut() was invoked");
        return ResponseEntity.ok()
                             .body(authenticationService.logOut());
    }

    @PatchMapping(path = "change-password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CARRIER') or hasRole('APPROVER') or hasRole('USER')")
    public ResponseEntity<Message> changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm) {
        log.debug("AuthController.changePassword(ChangePasswordForm changePasswordForm) was invoked");
        return ResponseEntity.ok()
                             .body(authenticationService.changePassword(changePasswordForm));
    }

    @GetMapping(path = "confirm-password")
    public ResponseEntity<Message> confirmPassword(@Valid @RequestParam("token") String token) {
        log.debug("AuthController.confirmPassword(String token) was invoked");
        return ResponseEntity.ok()
                             .body(authenticationService.confirmPassword(token));
    }

    @PostMapping("sign-in")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody SignInForm signInForm) {
        log.debug("AuthController.signIn(SignInForm signInForm) was invoked");
        return ResponseEntity.ok(authenticationService.signIn(signInForm));
    }
}
