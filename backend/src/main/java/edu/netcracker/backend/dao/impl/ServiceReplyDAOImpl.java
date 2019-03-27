package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.model.ServiceReply;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@PropertySource("classpath:sql/servicereplydao.properties")
public class ServiceReplyDAOImpl implements ServiceReplyDAO {

    private final JdbcTemplate jdbcTemplate;

    @Value("${DELETE_REPLY}")
    private String DELETE_REPLY;

    @Value("${INSERT_REPLY}")
    private String INSERT_REPLY;

    @Value("${GET_LAST_REPLY}")
    private String GET_LAST_REPLY;

    @Autowired
    public ServiceReplyDAOImpl(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public void save(ServiceReply reply) {
        jdbcTemplate.update(INSERT_REPLY,
                            reply.getServiceId(),
                            reply.getWriterId(),
                            reply.getReportText(),
                            reply.getCreationDate());
    }

    @Override
    public void delete(ServiceReply reply) {
        jdbcTemplate.update(DELETE_REPLY, reply.getServiceId(), reply.getWriterId());
    }

    @Override
    public Optional<String> getLastReply(Long id) {
        log.debug("Querying last reply of service with id = {}", id);
        try {
            String reply_text = jdbcTemplate.queryForObject(GET_LAST_REPLY, new Object[]{id, id}, String.class);
            return Optional.of(reply_text);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
