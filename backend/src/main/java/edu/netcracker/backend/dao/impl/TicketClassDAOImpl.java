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

    private static final String GET_ALL_TICKET_CLASSES_RELATED_TO_CARRIER = "SELECT ticket_class.* FROM user_a " +
            "INNER JOIN trip ON trip.carrier_id = user_a.user_id " +
            "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id " +
            "WHERE user_a.user_id = ?";

    private static final String GET_TICLET_CLASS_WITH_DISCOUNT = "SELECT ticket_class.* FROM user_a " +
            "INNER JOIN trip ON trip.carrier_id = user_a.user_id " +
            "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id " +
            "WHERE user_a.user_id = ? AND ticket_class.discount_id = ?";

    private static final String DELETE_DISCOUNT_CONNECTION = "UPDATE ticket_class " +
            "SET discount_id = null " +
            "WHERE class_id = ?";

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
                .map( ticketClassId -> new Object[]{ticketClassId})
                .collect(Collectors.toList()));
    }
}
