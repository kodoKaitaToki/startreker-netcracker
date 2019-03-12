package edu.netcracker.backend.dao.mapper.history;

import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.model.history.HistoryTrip;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryTicketMapper implements RowMapper<HistoryTicket> {

    @Override
    public HistoryTicket mapRow(ResultSet resultSet, int i) throws SQLException {
        HistoryTicket ticket = new HistoryTicket();
        HistoryTrip trip = new HistoryTrip();
        ticket.setTicketId(resultSet.getInt(1));
        ticket.setSeat(resultSet.getInt(2));
        ticket.setEndPrice(resultSet.getFloat(3));
        ticket.setPurchaseDate(resultSet.getTimestamp(4).toLocalDateTime());
        ticket.setClassName(resultSet.getString(5));
        trip.setDepartureDate(resultSet.getTimestamp(6).toLocalDateTime());
        trip.setArrivalDate(resultSet.getTimestamp(7).toLocalDateTime());
        trip.setDepartureSpaceportName(resultSet.getString(8));
        trip.setArrivalSpaceportName(resultSet.getString(9));
        trip.setDeparturePlanetName(resultSet.getString(10));
        trip.setArrivalPlanetName(resultSet.getString(11));
        trip.setCarrierName(resultSet.getString(12));
        ticket.setTrip(trip);
        return ticket;
    }
}
