package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoleDAO extends CrudDAO<Role> {

    Optional<Role> find(String roleName);

    Map<Integer, List<Role>> findAllRolesForUsers(List<Integer> userIds);
}
