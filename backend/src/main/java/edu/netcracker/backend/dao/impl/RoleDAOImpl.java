package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.TicketClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoleDAOImpl extends CrudDAOImpl<Role> implements RoleDAO {

    private static final String FIND_ROLE_BY_ROLE_NAME = "SELECT * FROM role_a WHERE role_name = ?";

    private static final String GET_ALL_ROLES_BELONG_TO_USERS = "SELECT "
                                                                + "assigned_role.user_id, "
                                                                + "role_a.role_id, "
                                                                + "role_a.role_name "
                                                                + "FROM assigned_role "
                                                                + "INNER JOIN role_a on assigned_role.role_id = role_a.role_id "
                                                                + "WHERE assigned_role.user_id IN (:userIds)";

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Role> find(String roleName) {
        logger.debug("Find role with role name: " + roleName);
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
        logger.debug("Find all roles that relates to users with ids: " + userIds);
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

