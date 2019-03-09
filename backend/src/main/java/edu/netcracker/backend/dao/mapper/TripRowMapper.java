package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TripRowMapper implements RowMapper<Trip> {
    private final TripStateRegistry tripStateRegistry;

    @Autowired
    public TripRowMapper(TripStateRegistry tripStateRegistry) {
        this.tripStateRegistry = tripStateRegistry;
    }

    @Override
    public Trip mapRow(ResultSet rs, int i) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(rs.getLong("trip_id"));
        trip.setTripState(tripStateRegistry.getState(rs.getInt("trip_status")));
        trip.setDepartureDate(rs.getTimestamp("departure_date")
                .toLocalDateTime());
        trip.setArrivalDate(rs.getTimestamp("arrival_date")
                .toLocalDateTime());
        trip.setCreationDate(rs.getTimestamp("creation_date")
                .toLocalDateTime());
        trip.setTripPhoto(rs.getString("trip_photo"));
        return trip;
    }
}