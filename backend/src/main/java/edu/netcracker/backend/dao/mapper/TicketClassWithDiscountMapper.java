package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.TicketClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketClassWithDiscountMapper implements RowMapper<TicketClass> {

    private DiscountMapper discountRowMapper;

    public TicketClassWithDiscountMapper(DiscountMapper discountRowMapper) {this.discountRowMapper = discountRowMapper;}

    @Override
    public TicketClass mapRow(ResultSet resultSet, int i) throws SQLException {
        TicketClass ticketClass = new TicketClass();

        ticketClass.setClassId(resultSet.getLong("class_id"));
        ticketClass.setClassName(resultSet.getString("class_name"));
        ticketClass.setTripId(resultSet.getLong("trip_id"));
        ticketClass.setClassSeats(resultSet.getInt("class_seats"));
        ticketClass.setDiscountId(resultSet.getLong("discount_id"));
        ticketClass.setTicketPrice(resultSet.getInt("ticket_price"));

        ticketClass.setDiscount(discountRowMapper.mapRow(resultSet, i));

        return ticketClass;
    }
}
