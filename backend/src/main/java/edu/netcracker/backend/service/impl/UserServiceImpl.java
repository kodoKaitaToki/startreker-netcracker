package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.message.request.SignUpForm;
import edu.netcracker.backend.message.request.UserCreateForm;
import edu.netcracker.backend.message.response.BoughtTicketDTO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.Ticket;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.UserInformationHolder;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.PasswordGeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    private PasswordEncoder passwordEncoder;

    private TicketDAO ticketDAO;

    private PossibleServiceDAO possibleServiceDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO,
                           PasswordEncoder passwordEncoder,
                           TicketDAO ticketDAO,
                           PossibleServiceDAO possibleServiceDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.ticketDAO = ticketDAO;
        this.possibleServiceDAO = possibleServiceDAO;
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public User find(Number id) {
        return userDAO.find(id)
                      .orElse(null);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public boolean ifUsernameExist(String username) {
        return userDAO.findByUsername(username)
                      .isPresent();
    }

    @Override
    public boolean ifEmailExist(String email) {
        return userDAO.findByEmail(email)
                      .isPresent();
    }

    @Override
    public User findByUsername(String userName) {
        return userDAO.findByUsername(userName)
                      .orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email)
                      .orElse(null);
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
        return userDAO.findByUsernameWithRole(userName, role)
                      .orElse(null);
    }

    @Override
    public User findByEmailWithRole(String email, Role role) {
        return userDAO.findByEmailWithRole(email, role)
                      .orElse(null);
    }

    @Override
    public User findByIdWithRole(Number id, Role role) {
        return userDAO.findByIdWithRole(id, role)
                      .orElse(null);
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
    public BoughtTicketDTO buyTicket(BoughtTicketDTO boughtTicketDTO) {
        log.debug("Getting ticket with id from TicketDAO");
        Optional<Ticket> optTicket = ticketDAO.find(boughtTicketDTO.getTicketId());

        log.debug("Getting user with id from UserDAO");
        Optional<User> optUser = userDAO.find(boughtTicketDTO.getPassengerId());

        List<PossibleService> possibleServices = new ArrayList<>();

        if (!optTicket.isPresent()) {
            log.error("Ticket with id {} not found", boughtTicketDTO.getTicketId());
            throw new RequestException(String.format("Ticket with id %s not found", boughtTicketDTO.getTicketId()),
                                       HttpStatus.NOT_FOUND);
        }

        if (!optUser.isPresent()) {
            log.error("User with id {} not found", boughtTicketDTO.getTicketId());
            throw new RequestException(String.format("User with id %s not found", boughtTicketDTO.getPassengerId()),
                                       HttpStatus.NOT_FOUND);
        }

        log.debug("Getting services by id");
        boughtTicketDTO.getPServicesId()
                       .forEach(id -> {
                           Optional<PossibleService> optPossibleService = possibleServiceDAO.find(id);

                           if (!optPossibleService.isPresent()) {
                               log.error("User with id {} not found", boughtTicketDTO.getTicketId());
                               throw new RequestException(String.format("Possible service with id %s not found",
                                                                        boughtTicketDTO.getPassengerId()),
                                                          HttpStatus.NOT_FOUND);
                           }

                           possibleServices.add(optPossibleService.get());
                       });

        Ticket ticket = optTicket.get();
        User user = optUser.get();

        ticketDAO.buyTicket(ticket, user);
        possibleServices.forEach(possibleService -> possibleServiceDAO.buyService(ticket, possibleService));

        return boughtTicketDTO;
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
        return userDAO.findByUsername(username)
                      .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles) {
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }
}