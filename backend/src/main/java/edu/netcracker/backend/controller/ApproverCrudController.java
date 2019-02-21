package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.UserForm;
import edu.netcracker.backend.message.response.UserDTO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.ApproverCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ApproverCrudController {

    private ApproverCrudService acs;

    @Autowired
    public ApproverCrudController(ApproverCrudService acs) {
        this.acs = acs;
    }

    @GetMapping("v1/api/admin/approvers/all")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDTO> getAllApprovers() {
        List<User> users = acs.getAllApprovers();
        return convertIntoDTO(users);
    }

    @GetMapping("v1/api/admin/approvers/paging")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDTO> getApprovers(@RequestParam("limit") Number limit, @RequestParam("offset") Number offset) {
        return convertIntoDTO(acs.getApprovers(limit, offset));
    }

    @GetMapping("v1/api/admin/approvers")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDTO getApprover(@RequestParam("id") Number id) {
        return UserDTO.from(acs.getById(id));
    }

    @PostMapping("v1/api/admin/approvers")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addApprover(@RequestBody UserForm approver) {
        acs.add(from(approver));
    }

    @PutMapping("v1/api/admin/approvers")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateApprover(@RequestBody UserForm approver) {
        acs.update(from(approver));
    }

    @DeleteMapping("v1/api/admin/approvers")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteApprover(@RequestBody UserForm approver) {
        acs.delete(from(approver));
    }

    @GetMapping("v1/api/admin/approvers/count")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BigInteger getRecordsCount() {
        return acs.getRecordsCount();
    }

    private List<UserDTO> convertIntoDTO(List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(UserDTO.from(user));
        }
        return userDTOS;
    }

    private static User from(UserForm approver) {
        User user = new User();
        user.setUserName(approver.getUsername());
        user.setUserEmail(approver.getEmail());
        user.setUserTelephone(approver.getTelephoneNumber());
        user.setUserIsActivated(approver.getIsActivated());
        return user;
    }
}
