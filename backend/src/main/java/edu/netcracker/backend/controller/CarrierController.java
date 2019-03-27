package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.UserCreateForm;
import edu.netcracker.backend.message.request.UserUpdateForm;
import edu.netcracker.backend.message.response.UserDTO;
import edu.netcracker.backend.service.CarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/api/v1/admin")
public class CarrierController {

    private CarrierService carrierService;

    @Autowired
    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @GetMapping(path = "/carrier-by-username")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getCarrierByUsername(@RequestParam("username") String username) {
        log.debug("CarrierController.getCarrierByUsername(String username) was invoked");
        return carrierService.getCarrierByUsername(username);
    }

    @GetMapping(path = "/carrier-by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getCarrierByEmail(@Valid @Email @RequestParam("email") String email) {
        log.debug("CarrierController.getCarrierByEmail(String email) was invoked");
        return carrierService.getCarrierByEmail(email);
    }

    @GetMapping(path = "/carrier/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getCarrierById(@PathVariable Integer userId) {
        log.debug("CarrierController.getCarrierById(Integer userId) was invoked");
        return carrierService.getCarrierById(userId);
    }

    @GetMapping(path = "/carrier/amount")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getCarrierAmount() {
        log.debug("CarrierController.getCarrierAmount() was invoked");
        return carrierService.getCarrierAmount();
    }

    @GetMapping(path = "/carrier")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllCarrier() {
        log.debug("CarrierController.getAllCarrier() was invoked");
        return carrierService.getAllCarrier();
    }

    @GetMapping(path = "/carrier-in-range-id")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllCarrier(@RequestParam("startId") Integer startId, @RequestParam("endId") Integer endId) {
        log.debug("CarrierController.getAllCarrier(Integer startId,  Integer endId) was invoked");
        return carrierService.getAllCarrier(startId, endId);
    }

    @GetMapping(path = "/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getPagination(@RequestParam("from") Integer from, @RequestParam("number") Integer number) {
        log.debug("CarrierController.getPagination(Integer from, Integer number) was invoked");
        return carrierService.getPagination(from, number);
    }

    @PostMapping(path = "/carrier")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO createCarrier(@Valid @RequestBody UserCreateForm createForm) {
        log.debug("CarrierController.createCarrier(UserCreateForm createForm) was invoked");
        return carrierService.createCarrier(createForm);
    }

    @DeleteMapping(path = "/carrier/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO deleteCarrier(@PathVariable Integer userId) {
        log.debug("CarrierController.deleteCarrier(Integer userId) was invoked");
        return carrierService.deleteCarrier(userId);
    }

    @PutMapping(path = "/carrier")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateCarrier(@Valid @RequestBody UserUpdateForm carrierForm) {
        log.debug("CarrierController.updateCarrier(UserUpdateForm carrierForm) was invoked");
        return carrierService.updateCarrier(carrierForm);
    }
}
