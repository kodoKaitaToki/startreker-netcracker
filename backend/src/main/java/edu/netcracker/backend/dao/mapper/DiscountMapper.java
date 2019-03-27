package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Discount;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DiscountMapper implements RowMapper<Discount> {

    @Override
    public Discount mapRow(ResultSet resultSet, int i) throws SQLException {
        Discount discount = new Discount();

        discount.setDiscountId(resultSet.getLong("discount_id"));
        discount.setStartDate(resultSet.getTimestamp("start_date")
                                       .toLocalDateTime());
        discount.setFinishDate(resultSet.getTimestamp("finish_date")
                                        .toLocalDateTime());
        discount.setDiscountRate(resultSet.getInt("discount_rate"));
        discount.setIsPercent(resultSet.getBoolean("is_percent"));

        return discount;
    }
}
