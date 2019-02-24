package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Setter
public class StatMapper implements RowMapper<CarrierStatisticsResponse> {

    @Override
    public CarrierStatisticsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        CarrierStatisticsResponse rstat = new CarrierStatisticsResponse();
        rstat.setSold(rs.getLong("sold"));
        rstat.setRevenue(rs.getLong("revenue"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date mon = null, sun = null;
        try {
            mon = rs.getDate("mon");
            sun = rs.getDate("sun");
        }catch (Exception ignored) {}
        if(mon != null) rstat.setFrom(df.format(mon));
        if(sun != null) rstat.setTo(df.format(sun));
        return rstat;
    }
}
