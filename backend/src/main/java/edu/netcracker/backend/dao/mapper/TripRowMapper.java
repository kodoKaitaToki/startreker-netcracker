package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Trip;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TripRowMapper implements RowMapper<Trip> {
    @Override
    public Trip mapRow(ResultSet resultSet, int i) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(resultSet.getLong(1));
        trip.setTripStatus(resultSet.getInt(2));
        trip.setDepartureDate(new Timestamp(resultSet.getDate(3).getTime()).toLocalDateTime());
        trip.setArrivalDate(new Timestamp(resultSet.getDate(4).getTime()).toLocalDateTime());
        trip.setTripPhoto(resultSet.getString(5));
        trip.setCreationDate(new Timestamp(resultSet.getDate(6).getTime()).toLocalDateTime());
        return trip;
    }
}
