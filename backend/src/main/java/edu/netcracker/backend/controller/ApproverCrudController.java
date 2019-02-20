package edu.netcracker.backend.controller;

import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.service.ApproverCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllApprovers() {
       return acs.getAllApprovers();
    }

    @GetMapping("v1/api/admin/approvers/paging")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getApprovers(@RequestParam("limit") Number limit, @RequestParam("offset") Number offset){
        return acs.getApprovers(limit, offset);
    }

    @GetMapping("v1/api/admin/approvers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getApprover(@RequestParam("id") Number id) {
        Optional<User> user = acs.getById(id);
        return user.orElse(null);
    }

    @PostMapping("v1/api/admin/approvers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addApprover(@RequestBody User approver) {
        acs.add(approver);
    }


    @PutMapping("v1/api/admin/approvers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateApprover(@RequestBody User approver) {
        acs.update(approver);
    }

    @DeleteMapping("v1/api/admin/approvers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteApprover(@RequestBody User approver) {
        acs.delete(approver);
    }


    @GetMapping("v1/api/admin/approvers/count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BigInteger getRecordsCount() {
        return acs.getRecordsCount();
    }

}
