package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ApproverDAO;
import edu.netcracker.backend.dao.mapper.ApproverRowMapper;
import edu.netcracker.backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@PropertySource("classpath:sql/approverdao.properties")
public class ApproverDAOImpl extends CrudDAOImpl<User> implements ApproverDAO {

    private final ApproverRowMapper rowMapper;
    @Value("${SELECT_ALL_APPROVERS}")
    private String SELECT_ALL_APPROVERS;
    @Value("${SELECT_APPROVER_BY_ID}")
    private String SELECT_APPROVER_BY_ID;
    @Value("${PAGING_SELECT}")
    private String PAGING_SELECT;
    @Value("${UPDATE_APPROVER}")
    private String UPDATE_APPROVER;
    @Value("${COUNT_APPROVERS}")
    private String COUNT_APPROVERS;

    public ApproverDAOImpl() {
        rowMapper = new ApproverRowMapper();
    }

    public List<User> findAllApprovers() {
        log.debug("Querying all approvers");
        List<User> approvers = new ArrayList<>();
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(SELECT_ALL_APPROVERS);

        log.debug("Mapping approvers");
        for (Map row : rows) {
            approvers.add((User) rowMapper.mapRow(row));
        }
        return approvers;
    }

    @Override
    public Optional<User> find(Number id) {
        log.debug("Querying approver with id: {}", id);
        try {
            User entity = (User) getJdbcTemplate().queryForObject(
                    SELECT_APPROVER_BY_ID,
                    new Object[]{id}, rowMapper);
            log.debug("Got approver with id: {}", id);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Got nothing");
            return Optional.empty();
        }
    }

    public List<User> find(Number limit, Number offset) {
        log.debug("Querying {} approvers from {}", limit, offset);

        List<User> approvers = new ArrayList<>();
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(PAGING_SELECT, limit, offset);

        log.debug("Mapping approvers");
        for (Map row : rows) {
            approvers.add((User) rowMapper.mapRow(row));
        }
        return approvers;
    }

    @Override
    public void update(User approver) {
        log.debug("Updating approver with id {}", approver.getUserId());
        getJdbcTemplate().update(UPDATE_APPROVER,
                approver.getUsername(),
                approver.getUserEmail(),
                approver.getUserTelephone(),
                approver.isUserIsActivated(),
                approver.getUserId()
        );
    }

    public BigInteger count() {
        BigInteger count = getJdbcTemplate().queryForObject(COUNT_APPROVERS, BigInteger.class);
        log.debug("Number of approvers: {}", count);
        return count;
    }
}
