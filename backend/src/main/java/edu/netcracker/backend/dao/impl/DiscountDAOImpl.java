package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.model.Discount;
import org.springframework.stereotype.Repository;


@Repository
public class DiscountDAOImpl extends CrudDAOImpl<Discount> implements DiscountDAO {

    private static final String DELETE_DISCOUNT_BY_ID = "DELETE FROM discount WHERE discount.discount_id = ?";

    @Override
    public void delete(Number discountId) {
        getJdbcTemplate().update(DELETE_DISCOUNT_BY_ID, discountId);
    }
}