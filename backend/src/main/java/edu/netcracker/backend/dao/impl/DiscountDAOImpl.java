package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.model.Discount;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DiscountDAOImpl extends CrudDAO<Discount> implements DiscountDAO {

    private static final String findSameDiscountSql = "SELECT * FROM discount WHERE " +
            "START_DATE = ? " +
            "AND FINISH_DATE = ? " +
            "AND DISCOUNT_RATE = ? " +
            "AND DISCOUNT_TYPE = ?";

    private static final String isAlreadyServiceHasDiscount = "SELECT COUNT(*) FROM discount_service WHERE p_service_id = ?";
    private static final String updateServiceDiscount = "UPDATE discount_service SET discount_id = ? where p_service_id = ?;" +
            "UPDATE discount_service SET discount_id = 2 where p_service_id = 14";
    private static final String insertServiceDiscount = "INSERT INTO discount_service (discount_id, p_service_id) VALUES (?, ?)";

    private static final String isAlreadyTicketClassHasDiscount = "SELECT COUNT(*) FROM discount_class WHERE class_id = ?";
    private static final String updateClassDiscount = "UPDATE discount_class SET discount_id = ? where class_id= ?";
    private static final String insertClassServiceDiscount = "INSERT INTO discount_class (discount_id, class_id) VALUES (?, ?)";

    @Override
    public void createDiscountForPossibleService(int possibleServiceId, Discount discount) {
        Discount currentDiscount = createDiscount(discount);
        String executeUpdate = isExistRecord(isAlreadyServiceHasDiscount, new Object[]{possibleServiceId}) ?
                updateServiceDiscount : insertServiceDiscount;

        execute(executeUpdate, currentDiscount.getDiscountId(), possibleServiceId);
    }

    @Override
    public void createDiscountForTicketClass(int ticketClassId, Discount discount) {
        Discount currentDiscount = createDiscount(discount);
        String executeUpdate = isExistRecord(isAlreadyTicketClassHasDiscount, new Object[]{ticketClassId}) ?
                updateClassDiscount :
                insertClassServiceDiscount;

        execute(executeUpdate, currentDiscount.getDiscountId(), ticketClassId);
    }

    public Optional<Discount> getSameDiscount(Discount discount) {
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

    private boolean isExistRecord(String query, Object[] values) {
        Long count = getJdbcTemplate().queryForObject(query, values, Long.class);
        return count > 0L;
    }

    private Discount createDiscount(Discount discount) {
        Optional<Discount> existingDiscount = getSameDiscount(discount);
        if (!existingDiscount.isPresent()) {
            save(discount);
        } else {
            discount = existingDiscount.get();
        }

        return discount;
    }

    private void execute(String query, int discountId, int entityId) {
        getJdbcTemplate().update(query, discountId, entityId);
    }
}