package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.mapper.ApproverRowMapper;
import edu.netcracker.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.Date;

@Component
public class ApproverDAO extends CrudDAO<User> {
    //role_id of approver role
    private final int APPROVER_ROLE_ID = 2;

    private final String SELECT_COLUMNS_FROM_USER = "SELECT u.user_id," +
            "user_name, " +
            "user_email, " +
            "user_telephone, " +
            "user_activated, " +
            "user_created " +
            "FROM user_a u ";

    private final String SELECT_ALL_APPROVERS = SELECT_COLUMNS_FROM_USER +
            "INNER JOIN assigned_role ar ON u.user_id = ar.user_id " +
            "INNER JOIN role_a r ON ar.role_id = r.role_id " +
            "WHERE r.role_id = 2 " +
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

    private final String INSERT_APPROVER = "INSERT INTO USER_A ( " +
            "USER_NAME, " +
            "USER_PASSWORD, " +
            "USER_EMAIL, " +
            "USER_TELEPHONE, " +
            "USER_ACTIVATED, " +
            "USER_CREATED " +
            ") " +
            "VALUES (?, ?, ?, ?, ?, statement_timestamp());";

    private final String ASSIGN_ROLE = "INSERT INTO assigned_role (user_id, role_id) VALUES (?, " +
            APPROVER_ROLE_ID
            + ")";

    private final String DELETE_APPROVER_ROLES = "DELETE FROM assigned_role WHERE user_id = ?";

    private ApproverRowMapper rowMapper;
    private final Logger logger = LoggerFactory.getLogger(ApproverDAO.class);

    public ApproverDAO() {
        rowMapper = new ApproverRowMapper();
    }


    public List<User> findAllApprovers() {
        logger.debug("Querying all approvers");
        List<User> approvers = new ArrayList<>();
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(SELECT_ALL_APPROVERS);

        logger.debug("Mapping approvers");
        for(Map row : rows) {
            approvers.add((User) rowMapper.mapRow(row));
        }
        return approvers;
    }

    @Override
    public Optional<User> find(Number id) {
        logger.info(String.format("Querying approver with id: %s", id));
        try{
            User entity = (User) getJdbcTemplate().queryForObject(
                    SELECT_APPROVER_BY_ID,
                    new Object[]{id}, rowMapper);
            logger.info("Got object");
            return Optional.ofNullable(entity);
        }catch (EmptyResultDataAccessException e){
            logger.info("Got nothing");
            return Optional.empty();
        }
    }

    @Override
    public void save(User entity) {
        getJdbcTemplate().update(INSERT_APPROVER,
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserEmail(),
                entity.getUserTelephone(),
                entity.isUserIsActivated()
        );
    }

    @Override
    public void update(User approver) {
        //TODO update of User
    }



}
