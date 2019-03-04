package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.Map;

public class ApproverRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        User usr = new User();
        usr.setUserId(rs.getInt("user_id"));
        usr.setUserName(rs.getString("user_name"));
        usr.setUserEmail(rs.getString("user_email"));
        usr.setUserTelephone(rs.getString("user_telephone"));
        usr.setUserIsActivated(rs.getBoolean("user_activated"));

        Timestamp timestamp = rs.getTimestamp("user_created");
        usr.setRegistrationDate(timestamp.toLocalDateTime());
        return usr;
    }

    public Object mapRow(Map row) {
        User usr = new User();
        Number id = (Number) row.get("user_id");
        usr.setUserId(id.intValue());
        usr.setUserName((String) row.get("user_name"));
        usr.setUserEmail((String) row.get("user_email"));
        usr.setUserTelephone((String) row.get("user_telephone"));
        usr.setUserIsActivated((Boolean) row.get("user_activated"));

        Timestamp date = (Timestamp) row.get("user_created");
        usr.setRegistrationDate(date.toLocalDateTime());
        return usr;
    }
}
