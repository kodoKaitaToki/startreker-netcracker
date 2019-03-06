package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.TicketClass;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {

    private static final String FIND_ROLE_BY_ROLE_NAME = "SELECT * FROM role_a WHERE role_name = ?";

    @Override
    public Optional<Role> find(String roleName) {
        try {
            Role role = getJdbcTemplate().queryForObject(
                    FIND_ROLE_BY_ROLE_NAME,
                    new Object[]{roleName},
                    getGenericMapper());

            return Optional.of(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

