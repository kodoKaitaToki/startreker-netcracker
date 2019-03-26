package edu.netcracker.backend.service;

import edu.netcracker.backend.model.User;

import java.math.BigInteger;
import java.util.List;

public interface ApproverCrudService {

    List<User> getAllApprovers();

    List<User> getApprovers(Number limit, Number offset);

    User getById(Number id);

    void add(User approver);

    void update(User approver);

    void delete(User approver);

    BigInteger getRecordsCount();
}
