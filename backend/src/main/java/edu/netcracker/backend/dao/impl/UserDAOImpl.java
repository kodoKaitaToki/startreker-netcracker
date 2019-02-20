package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDAOImpl extends CrudDAO<User> implements UserDetailsService, UserDAO {

    private final RoleDAO roleDAO;
    // Using * because dao is supposed to get all attributes and map them to java object
    private final String findByUsernameSql = "SELECT * FROM user_a WHERE user_name = ?";
    private final String findByEmailSql = "SELECT * FROM user_a WHERE user_email = ?";
    private final String findAllRolesSql = "SELECT role_id FROM assigned_role WHERE user_id = ?";
    private final String removeAllUserRolesSql = "DELETE FROM assigned_role WHERE user_id = ?";
    private final String addRoleSql = "INSERT INTO assigned_role (user_id, role_id) VALUES (?, ?)";
    private final String removeRoleSql = "DELETE FROM assigned_role WHERE user_id = ? AND role_id = ?";

    private final String findAll = "SELECT DISTINCT * FROM user_a";

    private final String findAllByRole = "SELECT DISTINCT * FROM user_a\n" +
            "  INNER JOIN assigned_role ON assigned_role.user_id = user_a.user_id\n" +
            "  INNER JOIN role ON assigned_role.role_id = role.role_id " +
            "  WHERE role_name = ?";

    private final String findPerPeriod = "SELECT * FROM user_a " +
            "WHERE user_created BETWEEN ? AND ?";

    private final String findPerPeriodByRole = "SELECT * FROM user_a " +
            "INNER JOIN assigned_role ON assigned_role.user_id = user_a.user_id " +
            "INNER JOIN role ON assigned_role.role_id = role.role_id " +
            "WHERE role_id = ? AND user_created BETWEEN ? AND ?";


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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username + " not found"));
    }

    public Optional<User> findByUsername(String userName) {
        return findSingleUserByAttributes(findByUsernameSql, new Object[]{userName});
    }

    public Optional<User> findByEmail(String email) {
        return findSingleUserByAttributes(findByEmailSql, new Object[]{email});
    }

    @Override
    public List<User> findAll() {
        ArrayList<User> users = new ArrayList<>();

        try {
            users.addAll(getJdbcTemplate().query(
                    findAll,
                    getGenericMapper()));

            for (User user: users) {
                attachRoles(user);
            }

        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
        }

        return users;
    }

    @Override
    public List<User> findAll(String roleName) {
        ArrayList<User> users = new ArrayList<>();

        try {
            users.addAll(getJdbcTemplate().query(
                    findAllByRole,
                    new Object[]{roleName},
                    getGenericMapper()));

            for (User user: users) {
                attachRoles(user);
            }

        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
        }

        return users;
    }


    @Override
    public List<User> findPerPeriod(LocalDate from, LocalDate to) {
        List<User> users = new ArrayList<>();
        users.addAll(getJdbcTemplate().query(
                findPerPeriod,
                new Object[]{from, to},
                getGenericMapper()));

        for (User user : users) {
            attachRoles(user);
        }

        return users;

    }

    @Override
    public List<User> findPerPeriodByRole(Number id, LocalDate from, LocalDate to) {
        List<User> users = new ArrayList<>();

        users.addAll(getJdbcTemplate().query(
                findPerPeriodByRole,
                new Object[]{id, from, to},
                getGenericMapper()));

        for (User user : users) {
            attachRoles(user);
        }

        return users;
    }

    @Override
    public void save(User user) {
        super.save(user);
        updateRoles(user);
    }

    @Override
    public void delete(User user) {
        getJdbcTemplate().update(removeAllUserRolesSql, user.getUserId());
        super.delete(user);
    }

    private Optional<User> attachRoles(User user) {
        List<Long> rows = getJdbcTemplate().queryForList(findAllRolesSql, Long.class, user.getUserId());
        List<Role> roles = new ArrayList<>();
        for (Long role_id : rows) {
            roles.add(roleDAO.find(role_id).orElse(null));
        }
        user.setUserRoles(roles);
        return Optional.of(user);
    }

    private Optional<User> findSingleUserByAttributes(String sql, Object[] attrs) {
        try{
            User user = getJdbcTemplate().queryForObject(
                    sql,
                    attrs,
                    getGenericMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    private void updateRoles(User user) {
        List<Integer> dbRoleIds = getJdbcTemplate().queryForList(findAllRolesSql, Integer.class, user.getUserId());
        List<Integer> userRoleIds = user.getUserRoles()
                .stream()
                .map(Role::getRoleId)
                .collect(Collectors.toList());
        for (Integer role_id : userRoleIds) {
            if (!dbRoleIds.contains(role_id)) {
                getJdbcTemplate().update(addRoleSql, user.getUserId(), role_id);
            }
        }
        for (Integer db_role : dbRoleIds) {
            if (!userRoleIds.contains(db_role)) {
                getJdbcTemplate().update(removeRoleSql, user.getUserId(), db_role);
            }
        }
    }
}
