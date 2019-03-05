package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.model.Service;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceDAOImpl extends CrudDAOImpl<Service> implements ServiceDAO {

    private final String FIND_ALL = "SELECT * FROM service";

    @Override
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();

        services.addAll(getJdbcTemplate().query(
                FIND_ALL,
                getGenericMapper()));

        return services;
    }
}
