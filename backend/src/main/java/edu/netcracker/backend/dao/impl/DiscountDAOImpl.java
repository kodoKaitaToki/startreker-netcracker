package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.model.Discount;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class DiscountDAOImpl extends CrudDAOImpl<Discount> implements DiscountDAO {

    private static final String DELETE_DISCOUNT_BY_ID = "DELETE FROM discount WHERE discount.discount_id = ?";

    @Override
    public void delete(Number discountId) {
        getJdbcTemplate().update(DELETE_DISCOUNT_BY_ID, discountId);
    }

    @Override
    public void deleteDiscounts(List<Long> discountIds) {
        getJdbcTemplate().batchUpdate(DELETE_DISCOUNT_BY_ID, discountIds.stream()
                .map(discountId -> new Object[]{discountId})
                .collect(Collectors.toList()));
    }
}