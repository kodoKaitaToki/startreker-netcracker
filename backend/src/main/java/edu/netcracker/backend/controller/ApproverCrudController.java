package edu.netcracker.backend.controller;

import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.service.ApproverCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ApproverCrudController {

    private ApproverCrudService acs;

    @Autowired
    public ApproverCrudController(ApproverCrudService acs) {
        this.acs = acs;
    }

    @RequestMapping("api/admin/approvers")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllApprovers() {
       return acs.getAllAprovers();
    }


    //TODO get approvers with specific range for pagination

    @RequestMapping("api/admin/approvers/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getApprover(@PathVariable Number id) {
        Optional<User> user = acs.getApprover(id);
        return user.orElse(null);
    }

    @PostMapping("api/admin/approvers/")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addApprover(@RequestBody User approver) {
        acs.addApprover(approver);
    }

    @PutMapping("api/admin/approvers/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateApprover(@PathVariable Number id, @RequestBody User approver) {
        acs.updateApprover(id, approver);
    }

    @DeleteMapping("api/admin/approvers/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteApprover(@PathVariable Number id, @RequestBody User approver) {
        acs.deleteApprover(id, approver);
    }

}
