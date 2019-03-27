package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.model.Discount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:sql/discountdao.properties")
public class DiscountDAOImpl extends CrudDAOImpl<Discount> implements DiscountDAO {

    @Value("${DELETE_DISCOUNT_BY_ID}")
    private String DELETE_DISCOUNT_BY_ID;

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