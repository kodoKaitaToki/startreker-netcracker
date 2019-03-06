package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.CarrierViewsResponse;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Setter
@Component
public class CarrierViewsMapper extends CarrierMapperHelper implements RowMapper<CarrierViewsResponse> {

    @Override
    public CarrierViewsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        CarrierViewsResponse rViews = new CarrierViewsResponse();
        rViews.setViews(rs.getLong("views"));

        mapTimeIntervals(rs, rViews);
        return rViews;
    }
}
