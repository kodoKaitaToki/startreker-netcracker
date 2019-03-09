package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TripMapper implements RowMapper<Trip> {
    @Override
    public Trip mapRow(ResultSet resultSet, int i) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(resultSet.getLong("trip_id"));
        trip.setTripStatus(resultSet.getInt("trip_status"));
        trip.setDepartureDate(new Timestamp(resultSet.getDate("departure_date").getTime()).toLocalDateTime());
        trip.setArrivalDate(new Timestamp(resultSet.getDate("arrival_date").getTime()).toLocalDateTime());
        trip.setTripPhoto(resultSet.getString("trip_photo"));
        trip.setCreationDate(new Timestamp(resultSet.getDate("creation_date").getTime()).toLocalDateTime());
        trip.setCarrierId(resultSet.getLong("carrier_id"));
        trip.setDeparturePlanet(new Planet(resultSet.getLong("departure_planet_id"),
                resultSet.getString("departure_planet_name")));
        trip.setArrivalPlanet(new Planet(resultSet.getLong("arrival_planet_id"),
                resultSet.getString("arrival_planet_name")));
        trip.setDepartureSpaceport(new Spaceport(resultSet.getLong("departure_spaceport_id"),
                resultSet.getString("departure_spaceport_name"), resultSet.getLong("departure_planet_id")));
        trip.setArrivalSpaceport(new Spaceport(resultSet.getLong("arrival_spaceport_id"),
                resultSet.getString("arrival_spaceport_name"), resultSet.getLong("arrival_planet_id")));
        return trip;
    }
}
