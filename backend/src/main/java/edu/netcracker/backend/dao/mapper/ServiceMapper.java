package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class ServiceMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        ServiceCRUDDTO service = new ServiceCRUDDTO();

        service.setId(rs.getLong("service_id"));
        service.setApproverName(rs.getString("user_name"));
        service.setServiceName(rs.getString("service_name"));
        service.setServiceDescription(rs.getString("service_description"));
        service.setServiceStatus(rs.getInt("service_status"));

        Date date = rs.getDate("creation_date");
        String strDate = date.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        service.setCreationDate(strDate);
        return service;
    }
}
