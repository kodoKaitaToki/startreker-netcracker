package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripWithArrivalAndDepartureDataMapper implements RowMapper<TripWithArrivalAndDepartureData> {

    @Override
    public TripWithArrivalAndDepartureData mapRow(ResultSet rs, int rowNum) throws SQLException {
        TripWithArrivalAndDepartureData trip =
                new TripWithArrivalAndDepartureData();

        trip.setTripId(rs.getLong("trip_id"));

        trip.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
        trip.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());

        trip.setArrivalSpacePort(rs.getString("arrival_spaceport_name"));
        trip.setDepartureSpacePort(rs.getString("departure_spaceport_name"));

        trip.setArrivalPlanet(rs.getString("arrival_planet_name"));
        trip.setDeparturePlanet(rs.getString("departure_planet_name"));

        return trip;
    }
}
