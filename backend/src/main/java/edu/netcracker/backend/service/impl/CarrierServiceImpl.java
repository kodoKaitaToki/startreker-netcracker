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

    @Autowired
    private UserService userService;

    @Override
    public UserDTO getCarrierByUsername(String username) {
        User user = userService.findByUsernameWithRole(username, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + username + " not found", HttpStatus.NOT_FOUND);
        }

        return UserDTO.from(user);
    }

    @Override
    public UserDTO getCarrierByEmail(String email) {
        User user = userService.findByEmailWithRole(email, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier with email " + email + " not found", HttpStatus.NOT_FOUND);
        }

        return UserDTO.from(user);
    }

    @Override
    public UserDTO getCarrierById(Integer userId) {
        User user = userService.findByIdWithRole(userId, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier with id " + userId + " not found", HttpStatus.NOT_FOUND);
        }

        return UserDTO.from(user);
    }

    @Override
    public List<UserDTO> getAllCarrier() {
        List<User> users = userService.findAllWithRole(AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            throw new RequestException("No carriers yet", HttpStatus.NOT_FOUND);
        }

        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllCarrier(Integer startId, Integer endId) {
        if (startId < 0 || endId < startId) {
            throw new RequestException("Invalid range", HttpStatus.BAD_REQUEST);
        }

        List<User> users = userService.findByRangeIdWithRole(startId, endId, AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            throw new RequestException("No carriers in id range yet", HttpStatus.NOT_FOUND);
        }

        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }



    @Override
    public Integer getCarrierAmount(){
        log.debug("CarrierService.getCarrierAmount() was invoked");
        return userService.getUserAmount(AuthorityUtils.ROLE_CARRIER);
    }

    @Override
    public List<UserDTO> getPagination(Integer from, Integer number) {
        if (from < 0 || number < 0) {
            throw new RequestException("Invalid range", HttpStatus.BAD_REQUEST);
        }

        List<User> users = userService.paginationWithRole(from, number, AuthorityUtils.ROLE_CARRIER);

        if (users.isEmpty()) {
            throw new RequestException("No carriers in range", HttpStatus.NOT_FOUND);
        }

        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @Override
    public UserDTO createCarrier(UserCreateForm createForm) {
        if (userService.ifUsernameExist(createForm.getUsername())) {
            throw new RequestException("Username already exist", HttpStatus.CONFLICT);
        }

        if (userService.ifEmailExist(createForm.getEmail())) {
            throw new RequestException("Email already exist", HttpStatus.CONFLICT);
        }

        User user = userService.createUser(createForm, carrierRoles());
        return UserDTO.from(user);
    }

    @Override
    public UserDTO deleteCarrier(Integer userId) {
        User user = userService.findByIdWithRole(userId, AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + userId + " not found ", HttpStatus.NOT_FOUND);
        }

        userService.delete(user);
        return UserDTO.from(user);
    }

    @Override
    public UserDTO updateCarrier(UserUpdateForm updateFrom) {
        User user = userService.findByIdWithRole(updateFrom.getUserId(), AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RequestException("Carrier " + updateFrom.getUserId() + " not found ", HttpStatus.NOT_FOUND);
        }

        if (!user.getUsername().equals(updateFrom.getUsername()) &&
                userService.ifUsernameExist(updateFrom.getUsername())) {
            throw new RequestException("Username already exist", HttpStatus.CONFLICT);
        }

        if (!user.getUserEmail().equals(updateFrom.getEmail()) &&
                userService.ifEmailExist(updateFrom.getEmail())) {
            throw new RequestException("Email already exist", HttpStatus.CONFLICT);
        }

        user.setUserEmail(updateFrom.getEmail());
        user.setUserTelephone(updateFrom.getTelephoneNumber());
        user.setUserName(updateFrom.getUsername());
        user.setUserIsActivated(updateFrom.getIsActivated());

        userService.save(user);

        return UserDTO.from(user);
    }

    private List<Role> carrierRoles() {
        return Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER);
    }

}