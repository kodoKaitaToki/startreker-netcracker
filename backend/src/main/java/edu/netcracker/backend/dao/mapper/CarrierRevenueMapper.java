package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Setter
public class CarrierRevenueMapper extends CarrierMapperHelper implements RowMapper<CarrierRevenueResponse>{

    @Override
    public CarrierRevenueResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        CarrierRevenueResponse rstat = new CarrierRevenueResponse();
        rstat.setSold(rs.getLong("sold"));
        rstat.setRevenue(rs.getLong("revenue"));

        mapTimeIntervals(rs, rstat);
        return rstat;
    }
}