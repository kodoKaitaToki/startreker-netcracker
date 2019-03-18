package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import lombok.extern.log4j.Log4j2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Log4j2
public class CarrierMapperHelper {

    protected void mapTimeIntervals(ResultSet rs, CarrierStatisticsResponse cResponse) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cResponse.setFrom(df.format(rs.getDate("mon")));
            cResponse.setTo(df.format(rs.getDate("sun")));
        } catch (SQLException e) {
            log.error(e);
        }
    }
}