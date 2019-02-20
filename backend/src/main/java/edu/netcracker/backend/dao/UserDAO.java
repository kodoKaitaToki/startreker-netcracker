package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    void save(User user);

    Optional<User> find(Number id);

    void delete(User user);

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findAll(String roleName);

    List<User> findPerPeriod(LocalDate from, LocalDate to);

    List<User> findPerPeriodByRole(Number id, LocalDate from, LocalDate to);
}
