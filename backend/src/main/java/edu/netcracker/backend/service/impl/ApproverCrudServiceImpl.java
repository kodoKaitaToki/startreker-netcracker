package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.ApproverDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.ApproverCrudService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ApproverCrudServiceImpl implements ApproverCrudService {

    private final ApproverDAO approverDAO;
    private final UserDAO userDAO;

    @Autowired
    public ApproverCrudServiceImpl(ApproverDAO approverDAO, UserDAO userDAO) {
        this.approverDAO = approverDAO;
        this.userDAO = userDAO;
    }

    public List<User> getAllApprovers() {
        return approverDAO.findAllApprovers();
    }

    public List<User> getApprovers(Number limit, Number offset) {
        return approverDAO.find(limit, offset);
    }

    public User getById(Number id) {
        return approverDAO.find(id).get();
    }

    public void add(User approver) {
        approver.getUserRoles().add(AuthorityUtils.ROLE_APPROVER);
        approver.setRegistrationDate(LocalDateTime.now(ZoneId.systemDefault()));
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
