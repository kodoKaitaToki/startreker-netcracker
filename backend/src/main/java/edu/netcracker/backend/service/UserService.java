package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.jwt.UserInformationHolder;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    boolean ifUsernameExist(String username);
    boolean ifEmailExist(String email);
    User getUserByEmail(String email);
    String changePasswordForUser(User user);
    User createUser(SignUpForm signUpForm, boolean isActivated);
    UserDetails createUserDetails(UserInformationHolder userInformationHolder);
}
