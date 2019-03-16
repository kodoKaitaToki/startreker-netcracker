package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.EmailFrom;
import edu.netcracker.backend.message.request.SignInForm;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.response.JwtResponse;
import edu.netcracker.backend.message.response.Message;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.JwtProvider;
import edu.netcracker.backend.service.AuthenticationService;
import edu.netcracker.backend.service.EmailService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
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
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User signUp(SignUpForm signUpForm, HttpServletRequest request) {
        if (userService.ifUsernameExist(signUpForm.getUsername())) {
            throw new RequestException("Username already exist", HttpStatus.CONFLICT);
        }

        if (userService.ifEmailExist(signUpForm.getEmail())) {
            throw new RequestException("Email already exist", HttpStatus.CONFLICT);
        }

        User user = userService.createUser(signUpForm, false, Collections.singletonList(AuthorityUtils.ROLE_USER));
        emailService.sendRegistrationMessage(signUpForm.getEmail(),
                                             jwtProvider.generateMailRegistrationToken(user.getUsername()));

        return user;
    }

    @Override
    public void passwordRecovery(EmailFrom emailFrom) {
        User user = userService.findByEmail(emailFrom.getEmail());

        if (user == null) {
            throw new RequestException("User not found", HttpStatus.NOT_FOUND);
        }

        String newUserPassword = userService.changePasswordForUser(user);

        emailService.sendPasswordRecoveryMessage(emailFrom.getEmail(), user.getUsername(), newUserPassword);
    }

    @Override
    public JwtResponse signIn(SignInForm signInForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInForm.getUsername(),
                signInForm.getPassword()));

        String accessToken = jwtProvider.generateAccessToken((UserDetails) authentication.getPrincipal());
        String refreshToken = jwtProvider.generateRefreshToken(signInForm.getUsername());

        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByUsername(signInForm.getUsername());

        if (user.getUserRefreshToken() != null) {
            refreshToken = user.getUserRefreshToken();
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
        if (!jwtProvider.validateToken(token) && !jwtProvider.isRegistrationToken(token)) {
            throw new RequestException("Invalid token", HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(jwtProvider.retrieveSubject(token));

        if (user == null) {
            throw new RequestException("User not found", HttpStatus.NOT_FOUND);
        }

        user.setUserIsActivated(true);
        userService.save(user);

        return new Message(HttpStatus.OK, "Password is confirmed");
    }

    @Override
    public Message logOut() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        if (user == null) {
            throw new RequestException("User not found", HttpStatus.NOT_FOUND);
        }

        user.setUserRefreshToken(null);
        userService.save(user);

        return new Message(HttpStatus.OK, "You are logged out");
    }
}
