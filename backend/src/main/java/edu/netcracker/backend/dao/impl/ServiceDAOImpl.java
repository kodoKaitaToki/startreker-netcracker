package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.PossibleServiceMapper;
import edu.netcracker.backend.model.Service;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceDAOImpl extends CrudDAO<Service> implements ServiceDAO {

    private RowMapper possibleServiceMapper = new PossibleServiceMapper();

    private final String findAll = "SELECT * FROM service";
    private final String findAllByTicketClass = "SELECT * FROM service " +
            "INNER JOIN possible_service ps on service.service_id = ps.service_id " +
            "WHERE ps.class_id = ?";

    @Override
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();

        services.addAll(getJdbcTemplate().query(
                findAll,
                getGenericMapper()));

        return services;
    }

    @Override
    public List<Service> findAllByTicketClass(Number id) {
        List<Service> services = new ArrayList<>();

        services.addAll(getJdbcTemplate().query(
                findAllByTicketClass,
                new Object[]{id},
                possibleServiceMapper));

        return services;
    }


}
