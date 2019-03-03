package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.message.response.ServiceDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ServiceMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        ServiceDTO service = new ServiceDTO();

        service.setId(rs.getLong("service_id"));
        service.setApproverName(rs.getString("user_name"));
        service.setServiceName(rs.getString("service_name"));
        service.setServiceDescription(rs.getString("service_description"));
        service.setServiceStatus(rs.getInt("service_status"));
        service.setCreationDate(rs.getDate("creation_date").toLocalDate());
        service.setReplyText(rs.getString("reply_text"));
        return service;
    }
}
