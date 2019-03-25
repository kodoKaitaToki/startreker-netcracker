package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
@PropertySource("classpath:sql/userdao.properties")
public class UserDAOImpl extends CrudDAOImpl<User> implements UserDAO {

    private final RoleDAO roleDAO;

    @Value("${FIND_BY_USERNAME_SQL}")
    private String FIND_BY_USERNAME_SQL;

    @Value("${FIND_BY_EMAIL_SQL}")
    private String FIND_BY_EMAIL_SQL;

    @Value("${FIND_ALL_ROLES_SQL}")
    private String FIND_ALL_ROLES_SQL;

    @Value("${REMOVE_ALL_USER_ROLES_SQL}")
    private String REMOVE_ALL_USER_ROLES_SQL;

    @Value("${ADD_ROLE_SQL}")
    private String ADD_ROLE_SQL;

    @Value("${REMOVE_ROLE_SQL}")
    private String REMOVE_ROLE_SQL;

    @Value("${FIND_BY_USERNAME_WITH_ROLE_SQL}")
    private String FIND_BY_USERNAME_WITH_ROLE_SQL;

    @Value("${FIND_BY_EMAIL_WITH_ROLE_SQL}")
    private String FIND_BY_EMAIL_WITH_ROLE_SQL;

    @Value("${FIND_ALL_BY_ROLE_SQL}")
    private String FIND_ALL_BY_ROLE_SQL;

    @Value("${FIND_ALL_BY_ROLE_IN_RANGE_SQL}")
    private String FIND_ALL_BY_ROLE_IN_RANGE_SQL;

    @Value("${FIND_BY_ROLE_WITH_ID_SQL}")
    private String FIND_BY_ROLE_WITH_ID_SQL;

    @Value("${PAGINATION_SQL}")
    private String PAGINATION_SQL;

    @Value("${FIND_PER_PERIOD}")
    private String FIND_PER_PERIOD;

    @Value("${FIND_PER_PERIOD_BY_ROLE}")
    private String FIND_PER_PERIOD_BY_ROLE;

    @Value("${USER_AMOUNT}")
    private String USER_AMOUNT;

    @Autowired
    public UserDAOImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<User> find(Number id) {
        Optional<User> userOpt = super.find(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return attachRoles(user);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String userName) {
        try {
            User user = getJdbcTemplate().queryForObject(FIND_BY_USERNAME_SQL, new Object[]{userName}, getGenericMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = getJdbcTemplate().queryForObject(FIND_BY_EMAIL_SQL, new Object[]{email}, getGenericMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsernameWithRole(String userName, Role role) {
        return executeSqlWithParam(FIND_BY_USERNAME_WITH_ROLE_SQL, new Object[]{role.getRoleName(), userName});
    }

    @Override
    public Optional<User> findByEmailWithRole(String email, Role role) {
        return executeSqlWithParam(FIND_BY_EMAIL_WITH_ROLE_SQL, new Object[]{role.getRoleName(), email});
    }

    @Override
    public Optional<User> findByIdWithRole(Number id, Role role) {
        return executeSqlWithParam(FIND_BY_ROLE_WITH_ID_SQL, new Object[]{role.getRoleName(), id});
    }

    @Override
    public List<User> findByRangeIdWithRole(Number startId, Number endId, Role role) {
        List<User> users = new ArrayList<>();

        users.addAll(getJdbcTemplate().query(FIND_ALL_BY_ROLE_IN_RANGE_SQL,
                                             new Object[]{role.getRoleName(), startId, endId},
                                             getGenericMapper()));

        attachRolesToUsers(users);

        return users;
    }

    @Override
    public List<User> findAllWithRole(Role role) {
        List<User> users = new ArrayList<>();

        users.addAll(getJdbcTemplate().query(FIND_ALL_BY_ROLE_SQL, new Object[]{role.getRoleName()}, getGenericMapper()));

        attachRolesToUsers(users);

        return users;
    }

    @Override
    public List<User> paginationWithRole(Integer from, Integer number, Role role) {
        List<User> users = new ArrayList<>();

        users.addAll(getJdbcTemplate().query(PAGINATION_SQL,
                                             new Object[]{role.getRoleName(), number, from},
                                             getGenericMapper()));

        attachRolesToUsers(users);

        return users;
    }

    @Override
    public List<User> findPerPeriod(LocalDateTime from, LocalDateTime to) {
        List<User> users = new ArrayList<>();
        users.addAll(getJdbcTemplate().query(FIND_PER_PERIOD, new Object[]{from, to}, getGenericMapper()));

        attachRolesToUsers(users);

        return users;
    }

    @Override
    public List<User> findPerPeriodByRole(Number id, LocalDateTime from, LocalDateTime to) {
        List<User> users = new ArrayList<>();

        users.addAll(getJdbcTemplate().query(FIND_PER_PERIOD_BY_ROLE, new Object[]{id, from, to}, getGenericMapper()));

        attachRolesToUsers(users);

        return users;
    }

    @Override
    public void save(User user) {
        super.save(user);
        updateRoles(user);
    }

    @Override
    public void delete(User user) {
        getJdbcTemplate().update(REMOVE_ALL_USER_ROLES_SQL, user.getUserId());
        super.delete(user);
    }

    @Override
    public Integer getUserAmount(Role role){
        log.debug("UserDAO.getUserAmount(Role role) was invoked with role={}", role.getRoleName());
        return getJdbcTemplate().queryForObject(USER_AMOUNT, new Object[]{role.getRoleName()}, Integer.class);
    }

    private Optional<User> executeSqlWithParam(String sql, Object[] params) {
        try {
            User user = getJdbcTemplate().queryForObject(sql, params, getGenericMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> attachRoles(User user) {
        List<Long> rows = getJdbcTemplate().queryForList(FIND_ALL_ROLES_SQL, Long.class, user.getUserId());
        user.setUserRoles(roleDAO.findIn(rows));
        return Optional.of(user);
    }

    private void attachRolesToUsers(List<User> users) {
        Map<Integer, List<Role>> relatedRoles = roleDAO.findAllRolesForUsers(users.stream()
                                                                                  .map(User::getUserId)
                                                                                  .collect(Collectors.toList()));
        for (User user : users) {
            user.setUserRoles(relatedRoles.get(user.getUserId()));
        }
    }

    private void updateRoles(User user) {
        List<Integer> dbRoleIds = getJdbcTemplate().queryForList(FIND_ALL_ROLES_SQL, Integer.class, user.getUserId());
        List<Integer> userRoleIds = user.getUserRoles()
                                        .stream()
                                        .map(Role::getRoleId)
                                        .collect(Collectors.toList());
        for (Integer role_id : userRoleIds) {
            if (!dbRoleIds.contains(role_id)) {
                getJdbcTemplate().update(ADD_ROLE_SQL, user.getUserId(), role_id);
            }
        }
        for (Integer db_role : dbRoleIds) {
            if (!userRoleIds.contains(db_role)) {
                getJdbcTemplate().update(REMOVE_ROLE_SQL, user.getUserId(), db_role);
            }
        }
    }
}
