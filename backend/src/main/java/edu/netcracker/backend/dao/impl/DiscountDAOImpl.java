package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.model.Discount;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DiscountDAOImpl extends CrudDAO<Discount> implements DiscountDAO {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DiscountDAOImpl.class);

    private static final String DELETE_NOT_CONNECTED_DISCOUNTS =
            "DELETE FROM discount " +
            "WHERE discount.discount_id IN ( " +
            "SELECT discount.discount_id " +
            "FROM discount " +
            "LEFT JOIN discount_class ON discount.discount_id = discount_class.discount_id " +
            "LEFT JOIN discount_service ON discount.discount_id = discount_service.discount_id " +
            "WHERE discount_class.class_id IS NULL AND discount_service.p_service_id IS NULL " +
            ")";

    private static final String DELETE_DISCOUNT_CONNECTIONS_FOR_POSSIBLE_SERVICES_FOR_TICKET_CLASS =
            "DELETE FROM discount_service " +
                    "WHERE discount_service.p_service_id IN ( " +
                    "SELECT possible_service.p_service_id FROM possible_service " +
                    "LEFT JOIN discount_service ON possible_service.p_service_id = discount_service.p_service_id " +
                    "WHERE possible_service.class_id = ? " +
                    ")";

    private static final String DELETE_DISCOUNT_CONNECTIONS_FOR_FOR_TICKET_CLASSES_FOR_TRIP =
            "DELETE FROM discount_class " +
                    "WHERE discount_class.class_id IN ( " +
                    "SELECT ticket_class.class_id FROM ticket_class " +
                    "LEFT JOIN discount_class ON ticket_class.class_id = discount_class.class_id " +
                    "WHERE ticket_class.trip_id = ? " +
                    ")";

    private static final String RETRIEVE_IDS_FROM_POSSIBLE_SERVICES_FOR_TICKET_CLASS =
            "SELECT possible_service.p_service_id FROM possible_service " +
                    "LEFT JOIN discount_service ON possible_service.p_service_id = discount_service.p_service_id " +
                    "WHERE possible_service.class_id = ?";

    private static final String RETRIEVE_IDS_FROM_TICKET_CLASS_FOR_TRIP =
            "SELECT ticket_class.class_id FROM ticket_class " +
                    "LEFT JOIN discount_class ON ticket_class.class_id = discount_class.class_id " +
                    "WHERE ticket_class.trip_id = ?";

    private static final String findSameDiscountSql = "SELECT * FROM discount WHERE " +
            "START_DATE = ? " +
            "AND FINISH_DATE = ? " +
            "AND DISCOUNT_RATE = ? " +
            "AND DISCOUNT_TYPE = ?";

    private static final String INSERT_SERVICE_DISCOUNT =
            "INSERT INTO discount_service (discount_id, p_service_id) VALUES (?, ?)";
    private static final String DELETE_SERVICE_DISCOUNT =
            "DELETE FROM discount_service where p_service_id = ?";

    private static final String INSERT_CLASS_DISCOUNT =
            "INSERT INTO discount_class (discount_id, class_id) VALUES (?, ?)";
    private static final String DELETE_CLASS_DISCOUNT =
            "DELETE FROM discount_class where class_id = ?";

    private static final String GET_DISCOUNT_FOR_POSSIBLE_SERVICE_BY_ID =
            "SELECT * FROM discount " +
                    "INNER JOIN discount_service ON discount.discount_id = discount_service.discount_id " +
                    "WHERE discount_service.p_service_id = ?";

    private static final String GET_DISCOUNT_FOR_TICKET_CLASS_BY_ID =
            "SELECT * FROM discount " +
                    "INNER JOIN discount_class ON discount.discount_id = discount_class.discount_id " +
                    "WHERE discount_class.class_id = ?";

    @Override
    public void createDiscountForPossibleService(int possibleServiceId, Discount discount) {
        createDiscount(discount);

        executeDelete(DELETE_SERVICE_DISCOUNT, possibleServiceId);

        executeInsert(INSERT_SERVICE_DISCOUNT,discount.getDiscountId(), possibleServiceId);
    }

    @Override
    public void createDiscountForTicketClass(int ticketClassId, Discount discount) {
       createDiscount(discount);

       executeDelete(DELETE_CLASS_DISCOUNT, ticketClassId);

       executeInsert(INSERT_CLASS_DISCOUNT, discount.getDiscountId(), ticketClassId);
    }

    @Override
    public void createDiscountForAllPossibleServicesForTicketClass(int ticketClassId, Discount discount) {
        createDiscount(discount);

        executeDelete(DELETE_DISCOUNT_CONNECTIONS_FOR_POSSIBLE_SERVICES_FOR_TICKET_CLASS, ticketClassId);

        List<Integer> ids = getIds(RETRIEVE_IDS_FROM_POSSIBLE_SERVICES_FOR_TICKET_CLASS, ticketClassId);

        executeBunchInsert(INSERT_SERVICE_DISCOUNT, discount.getDiscountId(), ids);
    }

    @Override
    public void createDiscountForAllTicketClassesForTrip(int tripId, Discount discount) {
        createDiscount(discount);

        executeDelete(DELETE_DISCOUNT_CONNECTIONS_FOR_FOR_TICKET_CLASSES_FOR_TRIP, tripId);

        List<Integer> ids = getIds(RETRIEVE_IDS_FROM_TICKET_CLASS_FOR_TRIP, tripId);

        executeBunchInsert(INSERT_CLASS_DISCOUNT, discount.getDiscountId(), ids);
    }

    @Override
    public Optional<Discount> getDiscountByPossibleServiceId(int possibleServiceId) {
        return getDiscount(GET_DISCOUNT_FOR_POSSIBLE_SERVICE_BY_ID, possibleServiceId);
    }

    @Override
    public Optional<Discount> getDiscountByTicketClassId(int ticketClassId) {
        return getDiscount(GET_DISCOUNT_FOR_TICKET_CLASS_BY_ID, ticketClassId);
    }

    @Override
    public void deleteDiscountForPossibleService(int possibleServiceId) {
        executeDelete(DELETE_SERVICE_DISCOUNT, possibleServiceId);
        deleteDiscounts();
    }

    @Override
    public void deleteDiscountForTicketClass(int ticketClassId) {
        executeDelete(DELETE_CLASS_DISCOUNT, ticketClassId);
        deleteDiscounts();
    }

    @Override
    public void deleteDiscountsForAllTicketClassesForTrip(int tripId) {
        executeDelete(DELETE_DISCOUNT_CONNECTIONS_FOR_FOR_TICKET_CLASSES_FOR_TRIP, tripId);
        deleteDiscounts();
    }

    @Override
    public void deleteDiscountsForAllPossibleServiceForClassTicket(int ticketClassId) {
        executeDelete(DELETE_DISCOUNT_CONNECTIONS_FOR_POSSIBLE_SERVICES_FOR_TICKET_CLASS, ticketClassId);
        deleteDiscounts();
    }

    private List<Integer> getIds(String query, int entityId) {
        return new ArrayList<>(getJdbcTemplate()
                .queryForList(query, new Object[]{entityId}, Integer.class));
    }

    private List<Integer> getListResult(String query, int entityId) {
        return new ArrayList<>(getJdbcTemplate()
                .queryForList(query, new Object[]{entityId}, Integer.class));
    }

    private void deleteDiscounts() {
        getJdbcTemplate().update(DELETE_NOT_CONNECTED_DISCOUNTS);
    }

    private Optional<Discount> getDiscount(String query, int entityId) {
        return getSingleResult(query, new Object[]{entityId});
    }

    private void executeInsert(String insertQuery, int discountId, int entityId) {
        getJdbcTemplate().update(insertQuery, discountId, entityId);
    }

    private void executeBunchInsert(String insertQuery, int discountId, List<Integer> entityIds) {
        getJdbcTemplate().batchUpdate(
                insertQuery,
                entityIds.stream().map(entityId -> new Object[]{discountId, entityId}).collect(Collectors.toList())
        );
    }

    private void executeDelete(String deleteQuery, int entityId) {
        getJdbcTemplate().update(deleteQuery, entityId);
    }

    private void createDiscount(Discount discount) {
        Optional<Discount> existingDiscount = getSameDiscount(discount);
        if (!existingDiscount.isPresent()) {
            save(discount);
        } else {
            discount.setDiscountId(existingDiscount.get().getDiscountId());
        }
    }

    private Optional<Discount> getSameDiscount(Discount discount) {
        return getSingleResult(findSameDiscountSql,  new Object[]{discount.getStartDate(),
                discount.getFinishDate(),
                discount.getDiscountRate(),
                discount.getDiscountType()});
    }

    private Optional<Discount> getSingleResult(String query, Object[] values) {
        try {
            Discount sameDiscount = getJdbcTemplate().queryForObject(
                    query,
                    values,
                    getGenericMapper());
            return sameDiscount != null ? Optional.of(sameDiscount) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}