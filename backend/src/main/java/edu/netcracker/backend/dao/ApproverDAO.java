package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class ApproverDAO extends CrudDAO<User> {
    //role_id of approver role
    private final int APPROVER_ROLE_ID = 2;

    private final String SELECT_COLUMNS_FROM_USER = "SELECT u.user_id," +
            "user_name, " +
            "user_email, " +
            "user_telephone, " +
            "user_activated, " +
            "user_created, " +
            "FROM user_a u ";

    private final String SELECT_ALL_APPROVERS = SELECT_COLUMNS_FROM_USER +
            "INNER JOIN assigned_role ar ON u.user_id = ar.user_id " +
            "INNER JOIN role_a r ON ar.role_id = r.role_id " +
            "WHERE r.role_name = 'role_approver' " +
            "ORDER BY u.user_id ASC ";

    private final String SELECT_APPROVER_BY_ID = SELECT_COLUMNS_FROM_USER +
            "WHERE u.user_id = ?";

    private final String UPDATE_APPROVER = "UPDATE user_a\n" +
            "  SET\n" +
            "\tuser_name = ?,\n" +
            "\tuser_email = ?,\n" +
            "\tuser_telephone = ?,\n" +
            "\tuser_activated = ?\n" +
            "WHERE user_id = ?;";

    private final String DELETE_APPROVER = "DELETE FROM user_a WHERE user_id = ?";

    private final String INSERT_APPROVER = "INSERT INTO USER_A (\n" +
            "\t  USER_NAME,\n" +
            "\t  USER_EMAIL,\n" +
            "\t  USER_TELEPHONE,\n" +
            "\t  USER_ACTIVATED,\n" +
            "\t  USER_CREATED\n" +
            ") VALUES (?, ?, ?, ?, ?); ";

    private final String ASSIGN_ROLE = "INSERT INTO assigned_role (user_id, role_id) VALUES (?, " +
            APPROVER_ROLE_ID
            + ")";

    private final String DELETE_APPROVER_ROLES = "DELETE FROM assigned_role WHERE user_id = ?";

    private final Logger logger = LoggerFactory.getLogger(ApproverDAO.class);


    public List<User> findAllApprovers() {
        logger.debug("Querying all approvers");
        return getJdbcTemplate().query(SELECT_ALL_APPROVERS, getGenericMapper());
    }

    @Override
    public Optional<User> find(Number id) {
        logger.info("Querying approver with id: %s", id);
        try{
            User entity = getJdbcTemplate().queryForObject(
                    SELECT_APPROVER_BY_ID,
                    new Object[]{id},
                    getGenericMapper());
            return Optional.ofNullable(entity);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public void update(User approver) {
        logger.info("preparing object", approver);
        getJdbcTemplate().update(UPDATE_APPROVER, resolveUpdateParameters(approver));
    }

}
