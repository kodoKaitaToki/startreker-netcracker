package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.model.TicketClass;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private static final String GET_ALL_TICKET_CLASSES_RELATED_TO_CARRIER = "SELECT "
                                                                            + "ticket_class.class_id, "
                                                                            + "ticket_class.class_name, "
                                                                            + "ticket_class.trip_id, "
                                                                            + "ticket_class.ticket_price, "
                                                                            + "ticket_class.discount_id, "
                                                                            + "ticket_class.class_seats "
                                                                            + "FROM user_a "
                                                                            + "INNER JOIN trip ON trip.carrier_id = user_a.user_id "
                                                                            + "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id "
                                                                            + "WHERE user_a.user_id = ?";
    private static final String GET_TICLET_CLASS_WITH_DISCOUNT = "SELECT "
                                                                 + "ticket_class.class_id, "
                                                                 + "ticket_class.class_name, "
                                                                 + "ticket_class.trip_id, "
                                                                 + "ticket_class.ticket_price, "
                                                                 + "ticket_class.discount_id, "
                                                                 + "ticket_class.class_seats "
                                                                 + "FROM user_a "
                                                                 + "INNER JOIN trip ON trip.carrier_id = user_a.user_id "
                                                                 + "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id "
                                                                 + "WHERE user_a.user_id = ? AND ticket_class.discount_id = ?";
    private static final String DELETE_DISCOUNT_CONNECTION =
            "UPDATE ticket_class " + "SET discount_id = null " + "WHERE class_id = ?";
    private final String SELECT_BY_TRIP_ID =
            "SELECT class_id, class_name, trip_id, ticket_price, discount_id, class_seats " +
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
        return getJdbcTemplate().query(SELECT_BY_TRIP_ID_WITH_ITEM_NUMBER,
                                       new Object[]{BundleId, TripId},
                                       (resultSet, i) -> {
                                           TicketClass tc = new TicketClass();
                                           tc.setClassId(resultSet.getLong(1));
                                           tc.setClassName(resultSet.getString(2));
                                           tc.setTripId(resultSet.getLong(3));
                                           tc.setTicketPrice(resultSet.getInt(4));
                                           tc.setItemNumber(resultSet.getInt(5));
                                           return tc;
                                       });
    }

    @Override
    public List<TicketClass> getAllTicketClassesRelatedToCarrier(Number carrierId) {
        return new ArrayList<>(getJdbcTemplate()
                .query(GET_ALL_TICKET_CLASSES_RELATED_TO_CARRIER,
                        new Object[]{carrierId},
                        getGenericMapper()));
    }

    public Optional<TicketClass> getTicketClassByDiscount(Number userId, Number discountId) {
        try {
            TicketClass user = getJdbcTemplate().queryForObject(
                    GET_TICLET_CLASS_WITH_DISCOUNT,
                    new Object[]{userId, discountId},
                    getGenericMapper());
            return user != null ? Optional.of(user) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteDiscountsForTicketClasses(List<Long> ticketClassIds) {
        getJdbcTemplate().batchUpdate(DELETE_DISCOUNT_CONNECTION, ticketClassIds.stream()
                                                                                .map(ticketClassId -> new Object[]{
                                                                                        ticketClassId})
                                                                                .collect(Collectors.toList()));
    }
}
