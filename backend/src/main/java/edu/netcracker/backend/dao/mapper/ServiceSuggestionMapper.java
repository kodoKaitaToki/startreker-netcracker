package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceSuggestionMapper extends PossibleServiceMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Service service = (Service) super.mapRow(resultSet, i);

        service.setDiscountRate(resultSet.getInt("discount_rate"));

        return service;
    }
}
