package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Discount;

public interface DiscountDAO extends CrudDAO<Discount>{

    void delete(Number discountId);
}
