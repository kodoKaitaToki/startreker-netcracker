package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.GenericMapper;
import edu.netcracker.backend.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceDAOImpl extends CrudDAO<Service> implements ServiceDAO {

    private final String FIND_ALL_SERVICE = "SELECT service_id FROM service;";
    private final String PAGING_FIND = "SELECT * FROM service LIMIT ? OFFSET ?;";
    private final String DELETE_SERVICE = "DELETE FROM service WHERE service_id = ?;";
    private final String UPDATE_SERVICE = "UPDATE service " +
            "SET service_name = ?, " +
            "service_description = ? " +
            "WHERE service_id = ?;";
    private final String INSERT_SERVICE = "INSERT INTO service " +
            "(service_id, service_name, service_description) " +
            "VALUES (?, ?, ?);";

    private final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    public ServiceDAOImpl() {

    }

    @Override
    public Optional<Service> find(Number id) {
        return Optional.empty();
    }

    @Override
    public void save(Service service) {

    }

    @Override
    public void delete(Service service) {

    }

    @Override
    protected void update(Service service) {

    }

    public List<Long> FindAllServiceId(){
        logger.debug("Querying all services_ids");
        List<Long> serviceIds = new ArrayList();

        logger.debug("Mapping services_ids");
        return serviceIds;
    }

}
