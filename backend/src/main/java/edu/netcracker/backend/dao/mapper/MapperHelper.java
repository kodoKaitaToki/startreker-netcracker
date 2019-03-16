package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MapperHelper {

    private final UserDAO userDAO;

    @Autowired
    public MapperHelper(UserDAO userDAO) {this.userDAO = userDAO;}

    public User mapUser(ResultSet rs, String prefix) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt(prefix + "_id"));

        if (user.getUserId() == 0) {
            return null;
        }

        user.setUserName(rs.getString(prefix + "_user_name"));
        user.setUserPassword(rs.getString(prefix + "_password"));
        user.setUserIsActivated(rs.getBoolean(prefix + "_activated"));
        user.setRegistrationDate(rs.getTimestamp(prefix + "_date_created")
                                   .toLocalDateTime());
        user.setUserRefreshToken(rs.getString(prefix + "_token"));
        user.setUserEmail(rs.getString(prefix + "_email"));
        user.setUserTelephone(rs.getString(prefix + "_telephone"));

        userDAO.attachRoles(user);

        return user;
    }
}
