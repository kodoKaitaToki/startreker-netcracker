package edu.netcracker.backend.model.service;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApproverCrudService {

    private CrudDAO<User> userDAO;
    private RoleDAO roleDAO;

    @Autowired
    public ApproverCrudService(CrudDAO<User> userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    /**
     * @return List<User> with approvers or empty list if there's not any approvers
     */
    public List<User> getAllAprovers() {
        //TODO: 16.02.2019 get list of approvers using {@link CrudDAO<User>} and {@link RoleDAO}
        return new ArrayList<>();
    }

    /**
     * @param id - id of required Approver
     * @return {@link Optional} with approver or empty if such approver doesn't existt
     */
    public Optional<User> getApprover(Long id) {
        //TODO: 16.02.2019   get approver with specified id
        return Optional.empty();
    }

    /**
     * @param approver - user with role of approver
     */
    public void addApprover(User approver) {
        //TODO: 16.02.2019   add approver to DB
    }

    /**
     * @param id - id of user to update
     */
    public void updateApprover(Long id, User approver) {
        //TODO: 16.02.2019   update user with the specified id
    }

    /**
     *
     * Deletes user by id
     * @param id - id of user to delete
     */
    public void deleteApprover(Long id) {
        // TODO: 16.02.2019  delete approver by id
    }
}
