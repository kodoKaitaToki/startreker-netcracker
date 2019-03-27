package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.User;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ApproverDAO {

    List<User> findAllApprovers();

    Optional<User> find(Number id);

    List<User> find(Number limit, Number offset);

    void update(User approver);

    BigInteger count();
}
