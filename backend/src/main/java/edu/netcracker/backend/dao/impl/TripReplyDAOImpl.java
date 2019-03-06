package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.model.TripReply;
import org.springframework.stereotype.Repository;

@Repository
public class TripReplyDAOImpl extends CrudDAOImpl<TripReply> implements TripReplyDAO {

    private static final String DELETE_REPLY = "DELETE FROM trip_reply " +
            "WHERE service_id = ? " +
            "AND writer_id = ?;";

    private static final String INSERT_REPLY = "INSERT INTO trip_reply ( " +
            "trip_id, " +
            "writer_id, " +
            "reply_text, " +
            "creation_date " +
            ") VALUES ( ?, ?, ?, ?);";

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
