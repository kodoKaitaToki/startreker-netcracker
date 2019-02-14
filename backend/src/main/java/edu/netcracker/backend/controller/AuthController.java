package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.message.request.EmailFrom;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.request.UserForm;
import edu.netcracker.backend.message.response.UserResponse;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.jwt.JwtProvider;
import edu.netcracker.backend.security.jwtResponse.JwtResponse;
import edu.netcracker.backend.model.service.EmailService;
import edu.netcracker.backend.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AuthController {

    private static final int ERROR_USER_ALREADY_EXISTS = -1;
    private static final int ERROR_MAIL_ALREADY_EXISTS = -2;
    private static final int ERROR_NO_SUCH_USER = -3;

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private UserDAO userDAO;
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider,
                          UserDAO userDAO,
                          UserService userService,
                          EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userDAO = userDAO;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(path = "/api/auth/sign-up")
    public UserResponse signUp(@Valid @RequestBody SignUpForm signUpForm, HttpServletRequest request){
        if (userService.ifUsernameExist(signUpForm.getUsername())) {
            throw new RequestException("Username already exist", ERROR_USER_ALREADY_EXISTS);
        }

        if (userService.ifEmailExist(signUpForm.getEmail())) {
            throw new RequestException("Email already exist", ERROR_MAIL_ALREADY_EXISTS);
        }

        User user = userService.createUser(signUpForm, false);

        emailService.sendRegistrationMessage(signUpForm.getEmail(),
                getContextPath(request),
                jwtProvider.generateMailRegistrationToken(user.getUsername()));

        return UserResponse.from(user);
    }

    @PostMapping(path = "/api/auth/password-recovery")
    public EmailFrom passwordRecovery(@Valid @RequestBody EmailFrom emailFrom) {
        User user = userService.getUserByEmail(emailFrom.getEmail());

        if (user == null) {
            throw new RequestException("No such user", ERROR_NO_SUCH_USER);
        }

        String newUserPassword = userService.changePasswordForUser(user);

        emailService.sendPasswordRecoveryMessage(emailFrom.getEmail(),
                user.getUsername(),
                newUserPassword);

        return emailFrom;
    }

    @PostMapping(path = "/api/log-out")
    public String logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RequestException("User is not authenticated!", 1);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> userOptional = userDAO.findByUsername(
                jwtProvider.retrieveSubject(userDetails.getUsername()));

        if (!userOptional.isPresent())
            throw new RequestException("No such user!", 2);

        User user = userOptional.get();
        user.setUserRefreshToken(null);
        userDAO.save(user);

        return "OK";
    }

    @GetMapping(path = "api/auth/confirmPassword")
    public ResponseEntity<String> confirmPassword(@Valid @RequestParam("token") String token) {

        if (!jwtProvider.validateToken(token))
            return ResponseEntity.created(null).body("NOT OK1");

        Optional<User> userOptional = userDAO.findByUsername(
                jwtProvider.retrieveSubject(token));

        if (!userOptional.isPresent())
            return ResponseEntity.created(null).body("NOT OK2");

        User user = userOptional.get();
        user.setUserIsActivated(true);
        userDAO.save(user);

        return ResponseEntity.created(null).body("OK");
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserForm signInForm) {

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(
                        signInForm.getUsername(),
                        signInForm.getPassword()));

        String jwt = jwtProvider.generateAuthenticationToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getUsername(), userDetails.getAuthorities()));
    }

    private String getContextPath(HttpServletRequest request) {
        // ne robet
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}
