package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@PropertySource("classpath:sql/roledao.properties")
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {

    @Value("${SELECT_ROLE_BY_NAME}")
    private String SELECT_ROLE_BY_NAME;

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        try {
            Role role = getJdbcTemplate().queryForObject(
                    SELECT_ROLE_BY_NAME,
                    new Object[]{roleName},
                    getGenericMapper());
            return Optional.ofNullable(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

