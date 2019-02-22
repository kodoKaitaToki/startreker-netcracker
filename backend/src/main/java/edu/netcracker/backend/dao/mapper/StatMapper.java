package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Setter
public class StatMapper implements RowMapper<CarrierStatisticsResponse> {

    @Override
    public CarrierStatisticsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        CarrierStatisticsResponse rstat = new CarrierStatisticsResponse();
        rstat.setSold(rs.getLong("sold"));
        rstat.setRevenue(rs.getLong("revenue"));
        return rstat;
    }
}
