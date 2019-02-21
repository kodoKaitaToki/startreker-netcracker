package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.UserCreateForm;
import edu.netcracker.backend.message.request.UserUpdateForm;
import edu.netcracker.backend.message.response.UserDTO;

import java.util.List;

public interface CarrierService {

    UserDTO getCarrierByUsername(String username);

    UserDTO getCarrierByEmail(String email);

    UserDTO getCarrierById(Integer userId);

    List<UserDTO> getAllCarrier();

    List<UserDTO> getAllCarrier(Integer startId, Integer endId);

    UserDTO createCarrier(UserCreateForm createForm);

    UserDTO deleteCarrier(Integer userId);

    UserDTO updateCarrier(UserUpdateForm updateFrom);

    List<UserDTO> getPagination(Integer start, Integer number);
}
