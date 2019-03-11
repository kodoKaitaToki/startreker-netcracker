package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Discount;

import java.util.List;

public interface DiscountDAO extends CrudDAO<Discount>{

    void delete(Number discountId);

    @Override
    List<Discount> findIn(List<?> args);

    void deleteDiscounts(List<Long> collect);
}
