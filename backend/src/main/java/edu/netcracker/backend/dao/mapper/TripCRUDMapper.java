package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripCRUDMapper implements RowMapper<Trip> {

    private final TripStateRegistry tripStateRegistry;

    @Autowired
    public TripCRUDMapper(TripStateRegistry tripStateRegistry) {
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
        trip.setOwner(mapUser(rs, "carrier"));
        trip.setApprover(mapUser(rs, "approver"));
        trip.setDeparturePlanet(new Planet(rs.getLong("departure_planet_id"), rs.getString("departure_planet_name")));
        trip.setArrivalPlanet(new Planet(rs.getLong("arrival_planet_id"), rs.getString("arrival_planet_name")));
        trip.setDepartureSpaceport(new Spaceport(rs.getLong("departure_spaceport_id"),
                                                 rs.getString("departure_spaceport_name"),
                                                 rs.getLong("departure_planet_id")));
        trip.setArrivalSpaceport(new Spaceport(rs.getLong("arrival_spaceport_id"),
                                               rs.getString("arrival_spaceport_name"),
                                               rs.getLong("arrival_planet_id")));

        return trip;
    }

    private User mapUser(ResultSet rs, String prefix) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt(prefix + "_id"));

        return user;
    }
}
