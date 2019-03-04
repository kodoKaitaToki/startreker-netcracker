package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.TripStateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripMapper implements RowMapper<Trip> {

    @Override
    public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(rs.getLong("trip_id"));
        trip.setTripState(TripStateUtils.getState(rs.getInt("trip_status")));
        trip.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
        trip.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
        trip.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        trip.setTripPhoto(rs.getString("trip_photo"));

        User owner = new User();
        owner.setUserId(rs.getInt("owner_id"));
        owner.setUserPassword(rs.getString("owner_password"));
        owner.setUserIsActivated(rs.getBoolean("owner_activated"));
        owner.setRegistrationDate(rs.getTimestamp("owner_date_created").toLocalDateTime());
        owner.setUserRefreshToken(rs.getString("owner_token"));
        owner.setUserEmail(rs.getString("owner_email"));
        owner.setUserTelephone(rs.getString("owner_telephone"));

        trip.setOwner(owner);

        int approverId = rs.getInt("approver_id");

        if(approverId != 0){
            User approver = new User();
            approver.setUserId(approverId);
            approver.setUserPassword(rs.getString("approver_password"));
            approver.setUserIsActivated(rs.getBoolean("approver_activated"));
            approver.setRegistrationDate(rs.getTimestamp("approver_date_created").toLocalDateTime());
            approver.setUserRefreshToken(rs.getString("approver_token"));
            approver.setUserEmail(rs.getString("approver_email"));
            approver.setUserTelephone(rs.getString("approver_telephone"));

            trip.setApprover(approver);
        }

        return trip;
    }
}
