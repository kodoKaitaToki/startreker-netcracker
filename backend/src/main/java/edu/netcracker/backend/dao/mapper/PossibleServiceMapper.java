package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.ServiceDescr;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PossibleServiceMapper implements RowMapper<PossibleService> {

    @Override
    public PossibleService mapRow(ResultSet rs, int rowNum) throws SQLException {
        PossibleService ps = new PossibleService();
        ps.setPServiceId(rs.getLong("p_service_id"));
        ps.setClassId(rs.getLong("class_id"));
        ps.setPServiceStatus(rs.getLong("p_service_status"));
        ps.setServiceId(rs.getLong("service_id"));
        ps.setServicePrice(rs.getLong("service_price"));

        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setApproverId(rs.getInt("approver_id"));
        serviceDescr.setCarrierId(rs.getInt("carrier_id"));
        serviceDescr.setCreationDate(rs.getTimestamp("creation_date")
                                       .toLocalDateTime());
        serviceDescr.setServiceDescription(rs.getString("service_description"));
        serviceDescr.setServiceName(rs.getString("service_name"));
        serviceDescr.setServiceStatus(rs.getInt("service_status"));

        ps.setService(serviceDescr);

        return ps;
    }
}
