package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.model.TicketClass;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketClassDAOImpl extends CrudDAOImpl<TicketClass> implements TicketClassDAO {

    private final String SELECT_BY_TRIP_ID = "SELECT class_id, trip_id, ticket_price " +
            "FROM ticket_class " +
            "WHERE trip_id = ?";

    @Override
    public List<TicketClass> findByTripId(Number id) {
        return getJdbcTemplate().query(SELECT_BY_TRIP_ID, new Object[]{id}, getGenericMapper());
    }
}
