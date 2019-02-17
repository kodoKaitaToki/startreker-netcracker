package edu.netcracker.backend.controller;

import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.service.ApproverCrudService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/admin/approvers")
    public List<User> getAllApprovers() {
        return acs.getAllAprovers();
    }

    @RequestMapping("/admin/approvers/{id}")
    public Optional<User> getApprover(@PathVariable Long id) {
        return acs.getApprover(id);
    }

    @PostMapping("/admin/approvers/")
    public void addApprover(@RequestBody User approver) {
        acs.addApprover(approver);
    }

    @PutMapping("admin/approvers/{id}")
    public void updateApprover(@PathVariable Long id, @RequestBody User approver) {
        acs.updateApprover(id, approver);
    }

    @DeleteMapping("admin/approvers/{id}")
    public void deleteApprover(@PathVariable Long id) {
        acs.deleteApprover(id);
    }

}
