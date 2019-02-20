package edu.netcracker.backend.model.service;

import edu.netcracker.backend.dao.ApproverDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.util.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ApproverCrudService {

    private ApproverDAO approverDAO;
    private UserDAO userDAO;

    @Autowired
    public ApproverCrudService(ApproverDAO approverDAO, UserDAO userDAO) {
        this.approverDAO = approverDAO;
        this.userDAO = userDAO;
    }

    public List<User> getAllApprovers() {
        return approverDAO.findAllApprovers();
    }

    public List<User> getApprovers(Number limit, Number offset) {
        return approverDAO.find(limit, offset);
    }

    public Optional<User> getById(Number id) {
        return approverDAO.find(id);
    }

    public void add(User approver) {
        approver.getUserRoles().add(AuthorityUtils.ROLE_APPROVER);
        approver.setRegistrationDate(LocalDate.now(ZoneId.systemDefault()));
        userDAO.save(approver);
    }

    public void update(User approver) {
        approverDAO.update(approver);
    }

    public void delete(User approver) {
        userDAO.delete(approver);
    }

    public BigInteger getRecordsCount() {
        return approverDAO.count();
    }
}
