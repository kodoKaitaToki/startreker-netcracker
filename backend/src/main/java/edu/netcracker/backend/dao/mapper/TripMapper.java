package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.state.trip.TripState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TripMapper implements RowMapper<Trip> {

    private final MapperHelper mapperHelper;

    @Autowired
    public TripMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    @Override
    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(rs.getLong("trip_id"));
        trip.setTripState(TripState.getState(rs.getInt("trip_status")));
        trip.setDepartureDate(rs.getTimestamp("departure_date")
                                .toLocalDateTime());
        trip.setArrivalDate(rs.getTimestamp("arrival_date")
                              .toLocalDateTime());
        trip.setCreationDate(rs.getTimestamp("creation_date")
                               .toLocalDateTime());
        trip.setTripPhoto(rs.getString("trip_photo"));

        trip.setOwner(mapperHelper.mapUser(rs, "owner"));
        trip.setApprover(mapperHelper.mapUser(rs, "approver"));

        trip.setDepartureSpaceport(mapSpaceport(rs, "departure_spaceport"));
        trip.setArrivalSpaceport(mapSpaceport(rs, "arrival_spaceport"));

        return trip;
    }

    private Spaceport mapSpaceport(ResultSet rs, String prefix) throws SQLException {
        Spaceport spaceport = new Spaceport();

        spaceport.setSpaceportId(rs.getLong(prefix + "_id"));
        spaceport.setSpaceportName(rs.getString(prefix + "_name"));
        spaceport.setCreationDate(rs.getTimestamp(prefix + "_creation_date")
                                    .toLocalDateTime());
        spaceport.setPlanet(mapPlanet(rs, prefix + "_planet"));
        return spaceport;
    }

    private Planet mapPlanet(ResultSet rs, String prefix) throws SQLException {
        Planet planet = new Planet();

        planet.setPlanetId(rs.getLong(prefix + "_id"));
        planet.setPlanetName(rs.getString(prefix + "_name"));

        return planet;
    }
}