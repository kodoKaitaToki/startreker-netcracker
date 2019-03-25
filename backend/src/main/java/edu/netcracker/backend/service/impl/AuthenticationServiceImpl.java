package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.ChangePasswordForm;
import edu.netcracker.backend.message.request.EmailFrom;
import edu.netcracker.backend.message.request.SignInForm;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.response.JwtResponse;
import edu.netcracker.backend.message.response.Message;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.JwtProvider;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.AuthenticationService;
import edu.netcracker.backend.service.EmailService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final SecurityContext securityContext;

    @Autowired
    public AuthenticationServiceImpl(UserService userService,
                                     EmailService emailService,
                                     AuthenticationManager authenticationManager,
                                     JwtProvider jwtProvider,
                                     SecurityContext securityContext) {
        this.userService = userService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.securityContext = securityContext;
    }

    @Override
    public User signUp(SignUpForm signUpForm, HttpServletRequest request) {
        log.debug("AuthenticationServiceImpl.signUp(SignUpForm signUpForm, HttpServletRequest request) was invoked");
        if (userService.ifUsernameExist(signUpForm.getUsername())) {
            log.error("User with username {} already exists", signUpForm.getUsername());
            throw new RequestException("Username already exists", HttpStatus.CONFLICT);
        }

        if (userService.ifEmailExist(signUpForm.getEmail())) {
            log.error("User with email {} already exists", signUpForm.getEmail());
            throw new RequestException("Email already exists", HttpStatus.CONFLICT);
        }

        User user = userService.createUser(signUpForm, false, Collections.singletonList(AuthorityUtils.ROLE_USER));
        emailService.sendRegistrationMessage(signUpForm.getEmail(),
                                             jwtProvider.generateMailRegistrationToken(user.getUsername()));

        log.debug("User {} was created", user.getUsername());

        return user;
    }

    @Override
    public void passwordRecovery(EmailFrom emailFrom) {
        log.debug("AuthenticationServiceImpl.passwordRecovery(EmailFrom emailFrom) was invoked");
        User user = userService.findByEmail(emailFrom.getEmail());

        if (user == null) {
            log.error("User with email {} not found", emailFrom.getEmail());
            throw new RequestException("User not found", HttpStatus.NOT_FOUND);
        }

        String newUserPassword = userService.changePasswordForUser(user);
        emailService.sendPasswordRecoveryMessage(emailFrom.getEmail(), user.getUsername(), newUserPassword);

        log.debug("Email with new password was sent to email address {}", emailFrom.getEmail());
    }

    @Override
    public JwtResponse signIn(SignInForm signInForm) {
        log.debug("AuthenticationServiceImpl.signIn(SignInForm signInForm) was invoked");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInForm.getUsername(),
                signInForm.getPassword()));

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(signInForm.getUsername());

        log.debug("User {} is signed in the app", user.getUsername());

        String accessToken = jwtProvider.generateAccessToken((UserDetails) authentication.getPrincipal());
        String refreshToken;

        if (user.getUserRefreshToken() != null) {
            refreshToken = user.getUserRefreshToken();
        } else {
            refreshToken = jwtProvider.generateRefreshToken(signInForm.getUsername());
        }

        user.setUserRefreshToken(refreshToken);
        userService.save(user);

        return new JwtResponse(accessToken,
                               refreshToken,
                               "Bearer",
                               userDetails.getUsername(),
                               userDetails.getAuthorities()
                                          .stream()
                                          .map(GrantedAuthority::getAuthority)
                                          .collect(Collectors.toList()));
    }

    @Override
    public Message confirmPassword(String token) {
        log.debug("AuthenticationServiceImpl.confirmPassword(String token) was invoked");
        if (!jwtProvider.validateToken(token) || !jwtProvider.isRegistrationToken(token)) {
            log.error("User sent invalid token {}", token);
            throw new RequestException("Invalid token", HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(jwtProvider.retrieveSubject(token));

        if (user == null) {
            log.error("User with username {} not found", jwtProvider.retrieveSubject(token));
            throw new RequestException("User not found", HttpStatus.NOT_FOUND);
        }

        user.setUserIsActivated(true);
        userService.save(user);

        log.debug("Registration for user: {} is confirmed", user.getUsername());

        return new Message(HttpStatus.OK, "Registration is confirmed");
    }

    @Override
    public Message logOut() {
        log.debug("AuthenticationServiceImpl.logOut() was invoked");
        User user = securityContext.getUser();

        user.setUserRefreshToken(null);
        userService.save(user);

        log.debug("User: {} is log outed", user.getUsername());

        return new Message(HttpStatus.OK, "You are logged out");
    }

    @Override
    public Message changePassword(ChangePasswordForm changePasswordForm) {
        log.debug("AuthenticationServiceImpl.changePassword(ChangePasswordForm changePasswordForm) was invoked");
        User user = securityContext.getUser();

        userService.changePasswordForUser(user, changePasswordForm);
        user.setUserRefreshToken(null);
        userService.save(user);

        log.debug("Password for user: {} is changed", user.getUsername());

        return new Message(HttpStatus.OK, "Password is changed");
    }
}
