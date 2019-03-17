package edu.netcracker.backend.dao.mapper.history;

import edu.netcracker.backend.model.history.HistoryService;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryServiceMapper implements RowMapper<HistoryService> {

    @Override
    public HistoryService mapRow(ResultSet resultSet, int i) throws SQLException {
        HistoryService hs = new HistoryService();
        hs.setServiceName(resultSet.getString(1));
        hs.setServiceCount(resultSet.getInt(2));
        return hs;
    }
}
