package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;

import java.util.Optional;

public interface RoleDAO extends CrudDAO<Role> {

    Optional<Role> find(String roleName);
}
