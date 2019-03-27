package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.dao.mapper.GenericMapper;
import edu.netcracker.backend.model.TripReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@PropertySource("classpath:sql/tripreplydao.properties")
public class TripReplyDAOImpl implements TripReplyDAO {

    private final JdbcTemplate jdbcTemplate;

    @Value("${DELETE_REPLY}")
    private String DELETE_REPLY;

    @Value("${INSERT_REPLY}")
    private String INSERT_REPLY;

    @Autowired
    public TripReplyDAOImpl(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public void save(TripReply reply) {
        jdbcTemplate.update(INSERT_REPLY,
                            reply.getTripId(),
                            reply.getWriterId(),
                            reply.getReportText(),
                            reply.getCreationDate());
    }

    @Override
    public void delete(TripReply reply) {
        jdbcTemplate.update(DELETE_REPLY, reply.getTripId(), reply.getWriterId());
    }

}
