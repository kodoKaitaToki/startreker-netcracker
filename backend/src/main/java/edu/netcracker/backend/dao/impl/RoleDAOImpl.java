package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
@PropertySource("classpath:sql/roledao.properties")
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {

    @Value("${FIND_ROLE_BY_ROLE_NAME}")
    private String FIND_ROLE_BY_ROLE_NAME;

    @Value("${GET_ALL_ROLES_BELONG_TO_USERS}")
    private String GET_ALL_ROLES_BELONG_TO_USERS;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Role> find(String roleName) {
        log.debug("Find role with role name: " + roleName);
        try {
            Role role = getJdbcTemplate().queryForObject(FIND_ROLE_BY_ROLE_NAME,
                                                         new Object[]{roleName},
                                                         getGenericMapper());

            return Optional.of(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Map<Integer, List<Role>> findAllRolesForUsers(List<Integer> userIds) {
        log.debug("Find all roles that relates to users with ids: " + userIds);
        Map<Integer, List<Role>> relatedRoles = new HashMap<>();

        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(GET_ALL_ROLES_BELONG_TO_USERS,
                                                                                 new MapSqlParameterSource("userIds",
                                                                                                           userIds));
        for (Map<String, Object> row : rows) {
            List<Role> roles = relatedRoles.computeIfAbsent((Integer) row.get("user_id"), id -> new ArrayList<>());
            roles.add(createRole(row));
        }

        return relatedRoles;
    }

    private Role createRole(Map<String, Object> row) {
        return Role.builder()
                   .roleId((Integer) row.get("role_id"))
                   .roleName((String) row.get("role_name"))
                   .build();
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}

