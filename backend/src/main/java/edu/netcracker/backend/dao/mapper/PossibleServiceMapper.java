package edu.netcracker.backend.dao.mapper;


import edu.netcracker.backend.model.Service;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PossibleServiceMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Service service = new Service();

        service.setServiceId(resultSet.getInt("service_id"));
        service.setServiceName(resultSet.getString("service_name"));
        service.setServiceDescription(resultSet.getString("service_description"));
        service.setServicePrice(resultSet.getInt("service_price"));

        return service;
    }
}
