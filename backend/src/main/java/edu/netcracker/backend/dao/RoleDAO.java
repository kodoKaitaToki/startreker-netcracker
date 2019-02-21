package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;

import java.util.Optional;

public interface RoleDAO {

    void save(Role role);

    Optional<Role> find(Number id);

    void delete(Role role);
}
