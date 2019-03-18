package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Oleh Demydenko
 */
@Component
public class BundleTripRowMapper implements RowMapper<Trip> {

    private final TripStateRegistry tripStateRegistry;
    private final Logger logger = LoggerFactory.getLogger(BundleTripRowMapper.class);

    @Autowired
    public BundleTripRowMapper(TripStateRegistry tripStateRegistry) {
        this.tripStateRegistry = tripStateRegistry;
    }

    @Override
    public Trip mapRow(ResultSet rs, int i) throws SQLException {
        logger.debug("Mapping trip");
        Trip t = new Trip();
        t.setTripId(rs.getLong("trip_id"));
        t.setTripState(tripStateRegistry.getState(rs.getInt("trip_status")));
        t.setDepartureDate(rs.getTimestamp("departure_date")
                             .toLocalDateTime());
        t.setArrivalDate(rs.getTimestamp("arrival_date")
                           .toLocalDateTime());
        t.setCreationDate(rs.getTimestamp("creation_date")
                            .toLocalDateTime());
        t.setTripPhoto(rs.getString("trip_photo"));

        t.setDepartureSpaceport(mapSpaceport(rs, "departure"));
        t.setArrivalSpaceport(mapSpaceport(rs, "arrival"));

        return t;
    }

    private Spaceport mapSpaceport(ResultSet rs, String prefix) throws SQLException {
        Spaceport sp = new Spaceport();
        sp.setSpaceportId(rs.getLong(prefix + "_" + "spaceport_id"));
        sp.setSpaceportName(rs.getString(prefix + "_" + "spaceport_name"));
        sp.setPlanet(mapPlanet(rs, prefix));
        return sp;
    }

    private Planet mapPlanet(ResultSet rs, String prefix) throws SQLException {
        Planet p = new Planet();
        p.setPlanetId(rs.getLong(prefix + "_" + "planet_id"));
        p.setPlanetName(rs.getString(prefix + "_" + "planet_name"));
        return p;
    }
}