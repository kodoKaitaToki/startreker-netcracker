package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Bundle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BundleRowMapper implements RowMapper<Bundle> {
    @Override
    public Bundle mapRow(ResultSet resultSet, int i) throws SQLException {
        Bundle b = new Bundle();
        b.setBundleId(resultSet.getLong(1));
        b.setStartDate(resultSet.getTimestamp(2).toLocalDateTime());
        b.setFinishDate(resultSet.getTimestamp(3).toLocalDateTime());
        b.setBundlePrice(resultSet.getInt(4));
        b.setBundleDescription(resultSet.getString(5));
        b.setBundlePhotoUri(resultSet.getString(6));
        return b;
    }
}
