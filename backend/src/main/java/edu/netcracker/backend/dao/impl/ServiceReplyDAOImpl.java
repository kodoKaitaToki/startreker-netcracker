package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.model.ServiceReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ServiceReplyDAOImpl implements ServiceReplyDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String DELETE_REPLY =
            "DELETE FROM service_reply " + "WHERE service_id = ? " + "AND writer_id = ?;";

    private static final String INSERT_REPLY = "INSERT INTO service_reply ( "
                                               + "service_id, "
                                               + "writer_id, "
                                               + "reply_text, "
                                               + "creation_date "
                                               + ") VALUES ( ?, ?, ?, ?);";

    private final String GET_LAST_REPLY = "SELECT reply_text\n"
                                          + "FROM service_reply\n"
                                          + "WHERE service_id = ?\n"
                                          + "AND creation_date = (SELECT MAX(creation_date)\n"
                                          + "                    FROM service_reply\n"
                                          + "                    WHERE service_id = ?)";

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

    private final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    @Override
    public void delete(ServiceReply reply) {
        jdbcTemplate.update(DELETE_REPLY, reply.getServiceId(), reply.getWriterId());
    }

    @Override
    public Optional<String> getLastReply(Long id) {
        logger.debug("Querying last reply of service with id = {}", id);
        try {
            String reply_text = jdbcTemplate.queryForObject(GET_LAST_REPLY, new Object[]{id, id}, String.class);
            return Optional.of(reply_text);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
