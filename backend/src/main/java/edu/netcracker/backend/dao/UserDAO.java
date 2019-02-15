package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDAO extends CrudDAO<User> implements UserDetailsService {

    private final RoleDAO roleDAO;
    private final String findByUsernameSql = "SELECT * FROM usr WHERE user_name = ?";
    private final String findByEmailSql = "SELECT * FROM usr WHERE user_email = ?";
    private final String findAllRolesSql = "SELECT role_id FROM assigned_role WHERE user_id = ?";
    private final String removeAllUserRolesSql = "DELETE FROM assigned_role WHERE user_id = ?";
    private final String addRoleSql = "INSERT INTO assigned_role (user_id, role_id) VALUES (?, ?)";
    private final String removeRoleSql = "DELETE FROM assigned_role WHERE user_id = ? AND role_id = ?";

    @Autowired
    public UserDAO(RoleDAO roleDAO) {
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
        return findBySingleAttribute(findByUsernameSql, userName);
    }

    public Optional<User> findByEmail(String email) {
        return findBySingleAttribute(findByEmailSql, email);
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

    private Optional<User> findBySingleAttribute(String sql, Object attr) {
        try{
            User user = getJdbcTemplate().queryForObject(
                    sql,
                    new Object[]{attr},
                    getGenericMapper());
            return user != null ? attachRoles(user) : Optional.empty();
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    private void updateRoles(User user) {
        List<Long> dbRoleIds = getJdbcTemplate().queryForList(findAllRolesSql, Long.class, user.getUserId());
        List<Long> userRoleIds = user.getUserRoles()
                .stream()
                .map(Role::getRoleId)
                .collect(Collectors.toList());
        for (Long role_id : userRoleIds) {
            if (!dbRoleIds.contains(role_id)) {
                getJdbcTemplate().update(addRoleSql, user.getUserId(), role_id);
            }
        }
        for (Long db_role : dbRoleIds) {
            if (!userRoleIds.contains(db_role)) {
                getJdbcTemplate().update(removeRoleSql, user.getUserId(), db_role);
            }
        }
    }
}
