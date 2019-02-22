package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;
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

    Optional<User> findByUsernameWithRole(String userName, Role role);

    Optional<User> findByEmailWithRole(String email, Role role);

    Optional<User> findByIdWithRole(Number id, Role role);

    List<User> findByRangeIdWithRole(Number startId, Number endId, Role role);

    List<User> findAllWithRole(Role role);

    List<User> paginationWithRole(Integer from, Integer number, Role role);

    List<User> findPerPeriod(LocalDate from, LocalDate to);

    List<User> findPerPeriodByRole(Number id, LocalDate from, LocalDate to);
}
