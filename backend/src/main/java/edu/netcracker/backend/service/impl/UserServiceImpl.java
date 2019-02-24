package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.request.UserCreateForm;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.UserInformationHolder;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.util.PasswordGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public User find(Number id) {
        return userDAO.find(id).orElse(null);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public boolean ifUsernameExist(String username) {
        return userDAO.findByUsername(username).isPresent();
    }

    @Override
    public boolean ifEmailExist(String email) {
        return userDAO.findByEmail(email).isPresent();
    }

    @Override
    public User findByUsername(String userName) {
        return userDAO.findByUsername(userName).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElse(null);
    }

    @Override
    public String changePasswordForUser(User user) {
        String newPassword = PasswordGeneratorUtils.generatePassword();

        user.setUserPassword(passwordEncoder.encode(newPassword));
        userDAO.save(user);

        return newPassword;
    }

    @Override
    public User createUser(SignUpForm signUpForm, boolean isActivated, List<Role> roles) {
        User user = new User(signUpForm.getUsername(),
                passwordEncoder.encode(signUpForm.getPassword()),
                signUpForm.getEmail());
        user.setUserIsActivated(isActivated);
        user.setUserRoles(roles);
        user.setUserTelephone(signUpForm.getTelephoneNumber());
        user.setRegistrationDate(LocalDateTime.now());
        userDAO.save(user);

        return user;
    }

    @Override
    public User createUser(UserCreateForm userCreateForm, List<Role> roles) {
        User user = new User(userCreateForm.getUsername(),
                passwordEncoder.encode(userCreateForm.getPassword()),
                userCreateForm.getEmail());
        user.setUserIsActivated(userCreateForm.getIsActivated());
        user.setUserRoles(roles);
        user.setUserTelephone(userCreateForm.getTelephoneNumber());
        user.setRegistrationDate(LocalDateTime.now());
        userDAO.save(user);

        return user;
    }

    @Override
    public User findByUsernameWithRole(String userName, Role role) {
        return userDAO.findByUsernameWithRole(userName, role).orElse(null);
    }

    @Override
    public User findByEmailWithRole(String email, Role role) {
        return userDAO.findByEmailWithRole(email, role).orElse(null);
    }

    @Override
    public User findByIdWithRole(Number id, Role role) {
        return userDAO.findByIdWithRole(id, role).orElse(null);
    }

    @Override
    public List<User> findByRangeIdWithRole(Number startId, Number endId, Role role) {
        return userDAO.findByRangeIdWithRole(startId, endId, role);
    }

    @Override
    public List<User> findAllWithRole(Role role) {
        return userDAO.findAllWithRole(role);
    }

    @Override
    public List<User> paginationWithRole(Integer from, Integer number, Role role) {
        return userDAO.paginationWithRole(from, number, role);
    }

    @Override
    public UserDetails createUserDetails(UserInformationHolder userInformationHolder) {
        if (userInformationHolder == null) {
            return null;
        }

        return new org.springframework.security.core.userdetails.User(userInformationHolder.getUsername(),
                userInformationHolder.getPassword(),
                mapRolesToAuthorities(userInformationHolder.getRoles()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username + " not found"));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}