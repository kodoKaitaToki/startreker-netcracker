package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CarrierMapperHelper {

    protected void mapTimeIntervals(ResultSet rs, CarrierStatisticsResponse cResponse){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date mon = null, sun = null;
        try {
            mon = rs.getDate("mon");
            sun = rs.getDate("sun");
        }catch (Exception ignored) {}
        if(mon != null) cResponse.setFrom(df.format(mon));
        if(sun != null) cResponse.setTo(df.format(sun));
    }
}
