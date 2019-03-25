package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.UserCreateForm;
import edu.netcracker.backend.message.request.UserUpdateForm;
import edu.netcracker.backend.message.response.UserDTO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.CarrierService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarrierServiceImpl implements CarrierService {

    private final UserService userService;

    @Autowired
    public CarrierServiceImpl(UserService userService) {this.userService = userService;}

    @Override
    public UserDTO getCarrierByUsername(String username) {
        log.debug("CarrierServiceImpl.getCarrierByUsername(String username) was invoked");
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            log.error("Carrier: {} not found", username);
            throw new RequestException("Carrier " + username + " not found", HttpStatus.NOT_FOUND);
        }

        return UserDTO.from(user);
    }

    @Override
    public UserDTO getCarrierByEmail(String email) {
        log.debug("CarrierServiceImpl.getCarrierByEmail(String email) was invoked");
        User user = userService.findByEmailWithRole(email, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            log.error("Carrier with email {} not found", email);
            throw new RequestException("Carrier with email " + email + " not found", HttpStatus.NOT_FOUND);
        }

        return UserDTO.from(user);
    }

    @Override
    public UserDTO getCarrierById(Integer userId) {
        log.debug("CarrierServiceImpl.getCarrierById(Integer userId) was invoked");
        User user = userService.findByIdWithRole(userId, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            log.error("Carrier with id {} not found", userId);
            throw new RequestException("Carrier with id " + userId + " not found", HttpStatus.NOT_FOUND);
        }

        return UserDTO.from(user);
    }

    @Override
    public List<UserDTO> getAllCarrier() {
        log.debug("CarrierServiceImpl.getAllCarrier() was invoked");
        List<User> users = userService.findAllWithRole(AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            log.error("No carriers in the system yet");
            throw new RequestException("No carriers yet", HttpStatus.NOT_FOUND);
        }

        return users.stream()
                    .map(UserDTO::from)
                    .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllCarrier(Integer startId, Integer endId) {
        log.debug("CarrierServiceImpl.getAllCarrier(Integer startId, Integer endId) was invoked");
        if (startId < 0 || endId < startId) {
            log.error("Invalid range for startId: {} and endId: {}", startId, endId);
            throw new RequestException("Invalid range", HttpStatus.BAD_REQUEST);
        }

        List<User> users = userService.findByRangeIdWithRole(startId, endId, AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            log.error("No carriers in id range startId: {} and endId: {}", startId, endId);
            throw new RequestException("No carriers in the id range yet", HttpStatus.NOT_FOUND);
        }

        return users.stream()
                    .map(UserDTO::from)
                    .collect(Collectors.toList());
    }


    @Override
    public Integer getCarrierAmount() {
        log.debug("CarrierService.getCarrierAmount() was invoked");
        return userService.getUserAmount(AuthorityUtils.ROLE_CARRIER);
    }

    @Override
    public List<UserDTO> getPagination(Integer from, Integer numberOfCarriers) {
        log.debug("CarrierServiceImpl.getPagination(Integer from, Integer numberOfCarriers) was invoked");
        if (from < 0 || numberOfCarriers < 0) {
            log.error("Invalid data for pagination, from: {} and numberOfCarriers: {}", from, numberOfCarriers);
            throw new RequestException("Invalid range", HttpStatus.BAD_REQUEST);
        }

        List<User> users = userService.paginationWithRole(from, numberOfCarriers, AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            log.error("No carriers for pagination, from: {} and numberOfCarriers: {}", from, numberOfCarriers);
            throw new RequestException("No carriers for pagination", HttpStatus.NOT_FOUND);
        }

        return users.stream()
                    .map(UserDTO::from)
                    .collect(Collectors.toList());
    }

    @Override
    public UserDTO createCarrier(UserCreateForm createForm) {
        log.debug("CarrierServiceImpl.createCarrier(UserCreateForm createForm) was invoked");
        if (userService.ifUsernameExist(createForm.getUsername())) {
            log.error("Carrier with username {} already exists", createForm.getUsername());
            throw new RequestException("Username already exist", HttpStatus.CONFLICT);
        }

        if (userService.ifEmailExist(createForm.getEmail())) {
            log.error("Carrier with email {} already exists", createForm.getEmail());
            throw new RequestException("Email already exist", HttpStatus.CONFLICT);
        }

        User user = userService.createUser(createForm, carrierRoles());
        log.debug("Carrier with username {} is created", user.getUsername());
        return UserDTO.from(user);
    }

    @Override
    public UserDTO deleteCarrier(Integer userId) {
        log.debug("CarrierServiceImpl.deleteCarrier(Integer userId) was invoked");
        User user = userService.findByIdWithRole(userId, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            log.error("Carrier with id {} not found", userId);
            throw new RequestException("Carrier " + userId + " not found", HttpStatus.NOT_FOUND);
        }

        userService.delete(user);
        return UserDTO.from(user);
    }

    @Override
    public UserDTO updateCarrier(UserUpdateForm updateFrom) {
        log.debug("CarrierServiceImpl.updateCarrier(UserUpdateForm updateFrom) was invoked");
        User user = userService.findByIdWithRole(updateFrom.getUserId(), AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            log.error("Carrier with id {} not found", updateFrom.getUserId());
            throw new RequestException("Carrier " + updateFrom.getUserId() + " not found ", HttpStatus.NOT_FOUND);
        }

        if (!user.getUsername()
                 .equals(updateFrom.getUsername()) && userService.ifUsernameExist(updateFrom.getUsername())) {
            log.error("Carrier with username {} already exists", updateFrom.getUsername());
            throw new RequestException("Username already exist", HttpStatus.CONFLICT);
        }

        if (!user.getUserEmail()
                 .equals(updateFrom.getEmail()) && userService.ifEmailExist(updateFrom.getEmail())) {
            log.error("Carrier with username {} already exists", updateFrom.getEmail());
            throw new RequestException("Email already exist", HttpStatus.CONFLICT);
        }

        user.setUserEmail(updateFrom.getEmail());
        user.setUserTelephone(updateFrom.getTelephoneNumber());
        user.setUserName(updateFrom.getUsername());
        user.setUserIsActivated(updateFrom.getIsActivated());

        userService.save(user);

        log.debug("Carrier with username {} is updated", user.getUsername());

        return UserDTO.from(user);
    }

    private List<Role> carrierRoles() {
        return Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER);
    }
}