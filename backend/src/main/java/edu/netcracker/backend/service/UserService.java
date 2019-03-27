package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.ChangePasswordForm;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.request.UserCreateForm;
import edu.netcracker.backend.message.response.BoughtTicketDTO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.UserInformationHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void save(User user);

    User find(Number id);

    void delete(User user);

    User findByUsername(String userName);

    User findByEmail(String email);

    UserDetails createUserDetails(UserInformationHolder userInformationHolder);

    boolean ifUsernameExist(String username);

    boolean ifEmailExist(String email);

    String changePasswordForUser(User user);

    void changePasswordForUser(User user, ChangePasswordForm changePasswordForm);

    User createUser(SignUpForm signUpForm, boolean isActivated, List<Role> roles);

    User createUser(UserCreateForm userCreateForm, List<Role> roles);

    User findByUsernameWithRole(String userName, Role role);

    User findByEmailWithRole(String email, Role role);

    User findByIdWithRole(Number id, Role role);

    List<User> findByRangeIdWithRole(Number startId, Number endId, Role role);

    List<User> findAllWithRole(Role role);

    List<User> paginationWithRole(Integer from, Integer number, Role role);

    BoughtTicketDTO buyTicket(BoughtTicketDTO boughtTicketDTO);

    Integer getUserAmount(Role role);
}
