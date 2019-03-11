package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.model.TripReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:sql/tripreplydao.properties")
public class TripReplyDAOImpl extends CrudDAOImpl<TripReply> implements TripReplyDAO {

    @Value("${DELETE_REPLY}")
    private String DELETE_REPLY;

    @Value("${INSERT_REPLY}")
    private String INSERT_REPLY;

    @Override
    public void save(TripReply reply) {
        getJdbcTemplate().update(INSERT_REPLY,
                                 reply.getTripId(),
                                 reply.getWriterId(),
                                 reply.getReportText(),
                                 reply.getCreationDate());
    }

    @Override
    public void delete(TripReply reply) {
        getJdbcTemplate().update(DELETE_REPLY, reply.getTripId(), reply.getWriterId());
    }
}
