package edu.netcracker.backend.service;


import edu.netcracker.backend.message.request.EmailFrom;
import edu.netcracker.backend.message.request.SignInForm;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.response.JwtResponse;
import edu.netcracker.backend.message.response.Message;
import edu.netcracker.backend.model.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    User signUp(SignUpForm signUpForm, HttpServletRequest request);

    void passwordRecovery(EmailFrom emailFrom);

    JwtResponse signIn(SignInForm signInForm);

    Message confirmPassword(String token);

    Message logOut();
}
