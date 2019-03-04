package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.model.TicketClass;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketClassDAOImpl extends CrudDAOImpl<TicketClass> implements TicketClassDAO {

    private static final String SELECT_BY_TRIP_ID_WITH_ITEM_NUMBER = "SELECT " +
            "tc.class_id, " +
            "class_name, " +
            "trip_id, " +
            "ticket_price, " +
            "bc.item_number " +
            "FROM ticket_class tc " +
            "INNER JOIN bundle_class bc on tc.class_id = bc.class_id " +
            "WHERE bc.bundle_id = ? AND trip_id = ?;";

    private final String SELECT_BY_TRIP_ID = "SELECT class_id, class_name, trip_id, ticket_price " +
            "FROM ticket_class " +
            "WHERE trip_id = ?";


    @Override
    public List<TicketClass> findByTripId(Number id) {
        return getJdbcTemplate().query(SELECT_BY_TRIP_ID, new Object[]{id}, getGenericMapper());
    }

    /**
     * Sophisticated method for selecting ticketClasses with item_number
     *
     * @param BundleId - id of bundle
     * @param TripId   - id of trip
     * @return list of ticket classes with item_number required for bundles
     */
    public List<TicketClass> findTicketClassWithItemNumber(Number BundleId, Number TripId) {
        return getJdbcTemplate().query(SELECT_BY_TRIP_ID_WITH_ITEM_NUMBER, new Object[]{BundleId, TripId}, (resultSet, i) -> {
            TicketClass tc = new TicketClass();
            tc.setClassId(resultSet.getLong(1));
            tc.setClassName(resultSet.getString(2));
            tc.setTripId(resultSet.getLong(3));
            tc.setTicketPrice(resultSet.getInt(4));
            tc.setItemNumber(resultSet.getInt(5));
            return tc;
        });
    }
}
