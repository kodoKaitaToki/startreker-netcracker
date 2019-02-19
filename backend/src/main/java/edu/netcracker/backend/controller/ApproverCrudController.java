package edu.netcracker.backend.controller;

import edu.netcracker.backend.dao.ApproverDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
public class ApproverCrudController {

    private ApproverDAO approverDAO;
    private UserDAO userDAO;

    @Autowired
    public ApproverCrudController(ApproverDAO approverDAO, UserDAO userDAO) {
        this.approverDAO = approverDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping("api/admin/approvers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllApprovers() {
       return approverDAO.findAllApprovers();
    }

    @GetMapping("api/admin/approvers/{limit}/{offset}")
    public List<User> getApprovers(@PathVariable Number limit, @PathVariable Number offset){
        return approverDAO.find(limit, offset);
    }

    @RequestMapping("api/admin/approvers/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getApprover(@PathVariable Number id) {
        Optional<User> user = approverDAO.find(id);
        return user.orElse(null);
    }

    @PostMapping("api/admin/approvers/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addApprover(@RequestBody User approver) {
        approverDAO.save(approver);
    }

    @PutMapping("api/admin/approvers/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateApprover(@RequestBody User approver) {
        approverDAO.update(approver);
    }

    @DeleteMapping("api/admin/approvers/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteApprover(@RequestBody User approver) {
        userDAO.delete(approver);
    }

    @GetMapping("api/admin/approvers/count")
    public BigInteger getRecordsCount() {
        return approverDAO.count();
    }

}
