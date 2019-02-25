package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.impl.CrudDAOImpl;
import edu.netcracker.backend.dao.mapper.ApproverRowMapper;
import edu.netcracker.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ApproverDAO extends CrudDAOImpl<User> {

    private final String SELECT_COLUMNS_FROM_USER = "SELECT u.user_id, " +
            "user_name, " +
            "user_email, " +
            "user_telephone, " +
            "user_activated, " +
            "user_created " +
            "FROM user_a u ";

    private final String SELECT_ALL_APPROVERS = SELECT_COLUMNS_FROM_USER +
            "INNER JOIN assigned_role ar ON u.user_id = ar.user_id " +
            "INNER JOIN role_a r ON ar.role_id = r.role_id " +
            "WHERE r.role_name = 'ROLE_APPROVER' " +
            "ORDER BY u.user_id ";

    private final String SELECT_APPROVER_BY_ID = SELECT_COLUMNS_FROM_USER +
            "WHERE u.user_id = ?";

    private final String UPDATE_APPROVER = "UPDATE user_a\n" +
            "SET " +
            "user_name = ?, " +
            "user_email = ?, " +
            "user_telephone = ?, " +
            "user_activated = ? " +
            "WHERE user_id = ?;";

    private final String INSERT_APPROVER = "INSERT INTO USER_A ( " +
            "USER_NAME, " +
            "USER_PASSWORD, " +
            "USER_EMAIL, " +
            "USER_TELEPHONE, " +
            "USER_ACTIVATED, " +
            "USER_CREATED " +
            ") " +
            "VALUES (?, ?, ?, ?, ?, ?);";

    private final String PAGING_SELECT = SELECT_ALL_APPROVERS + "LIMIT ? OFFSET ?;";

    private final String COUNT_STATEMENT = "SELECT count(*) FROM user_a u " +
            "INNER JOIN assigned_role ar ON u.user_id = ar.user_id " +
            "INNER JOIN role_a r ON ar.role_id = r.role_id " +
            "WHERE r.role_name = 'ROLE_APPROVER';";

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
        for (Map row : rows) {
            approvers.add((User) rowMapper.mapRow(row));
        }
        return approvers;
    }

    @Override
    public Optional<User> find(Number id) {
        logger.info(String.format("Querying approver with id: %s", id));
        try {
            User entity = (User) getJdbcTemplate().queryForObject(
                    SELECT_APPROVER_BY_ID,
                    new Object[]{id}, rowMapper);
            logger.info("Got object");
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Got nothing");
            return Optional.empty();
        }
    }

    /**
     * Finds specified number of approvers starting from offset number
     *
     * @param limit  - offset number
     * @param offset - number of rows to select
     * @return list of users
     */
    public List<User> find(Number limit, Number offset) {
        logger.debug(String.format("Querying %s approvers from %s", limit, offset));

        List<User> approvers = new ArrayList<>();
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(PAGING_SELECT, limit, offset);

        logger.debug("Mapping approvers");
        for (Map row : rows) {
            approvers.add((User) rowMapper.mapRow(row));
        }
        return approvers;
    }

    @Override
    public void update(User approver) {
        getJdbcTemplate().update(UPDATE_APPROVER,
                approver.getUsername(),
                approver.getUserEmail(),
                approver.getUserTelephone(),
                approver.isUserIsActivated(),
                approver.getUserId()
        );
    }

    public BigInteger count() {
        return getJdbcTemplate().queryForObject(COUNT_STATEMENT, BigInteger.class);
    }
}
