package edu.netcracker.backend.model.service;

import edu.netcracker.backend.dao.ApproverDAO;
import edu.netcracker.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApproverCrudService {

    private ApproverDAO approverDAO;

    private Logger logger = LoggerFactory.getLogger(ApproverCrudService.class);

    @Autowired
    public ApproverCrudService(ApproverDAO approverDAO) {
        this.approverDAO = approverDAO;
    }

    /**
     * @return List<User> with approvers or empty list if there's not any approvers
     */
    public List<User> getAllAprovers() {
        logger.info("getting all approvers.");
        return approverDAO.findAllApprovers();
    }

    /**
     * @param id - id of required Approver
     * @return {@link Optional} with approver or empty if such approver doesn't existt
     */
    public Optional<User> getApprover(Number id) {
        logger.info(String.format("getting approver with id: %s.", id));
        return approverDAO.find(id);
    }

    /**
     * @param approver - user with role of approver
     */
    public void addApprover(User approver) {
        logger.info("saving approver.");
        approverDAO.save(approver);
    }

    /**
     * @param id - id of user to update
     */
    public void updateApprover(User approver) {
        logger.info("updating approver %s with id: ");
        approverDAO.update(approver);
    }

    /**
     *
     * Deletes user by id
     * @param id - id of user to delete
     * @param approver - approver to delete
     */
    public void deleteApprover(Number id, User approver) {
        logger.info("deleting approver with id");
        approverDAO.delete(approver);
    }
}
