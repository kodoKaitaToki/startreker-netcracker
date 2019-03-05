package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.model.ServiceReply;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceReplyDAOImpl extends CrudDAOImpl<ServiceReply> implements ServiceReplyDAO {
    private static final String DELETE_REPLY = "DELETE FROM service_reply " +
            "WHERE service_id = ? " +
            "AND writer_id = ?;";

    private static final String INSERT_REPLY = "INSERT INTO service_reply ( " +
            "service_id, " +
            "writer_id, " +
            "reply_text, " +
            "creation_date " +
            ") VALUES ( ?, ?, ?, ?);";

    @Override
    public void save(ServiceReply reply) {
        getJdbcTemplate().update(INSERT_REPLY,
                reply.getServiceId(),
                reply.getWriterId(),
                reply.getReportText(),
                reply.getCreationDate());
    }

    @Override
    public void delete(ServiceReply reply) {
        getJdbcTemplate().update(DELETE_REPLY, reply.getServiceId(), reply.getWriterId());
    }
}
