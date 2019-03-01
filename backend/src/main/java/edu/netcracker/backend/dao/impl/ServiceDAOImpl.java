package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.model.ServiceDescr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceDAOImpl extends CrudDAO<ServiceDescr> implements ServiceDAO {

    //private final String FIND_SERVICE = "SELECT * FROM service WHERE service_id = ?;";
    private final String FIND_SERVICE_BY_NAME = "";
    private final String FIND_ALL_SERVICES = "";
    private final String FIND_PAGIN_SERVICES = "";
    private final String DELETE_SERVICE = "";
    /*private final String UPDATE_SERVICE = "UPDATE service " +
            "SET service_name = ?, " +
            "service_description = ? " +
            "WHERE service_id = ?;";
    /*private final String INSERT_SERVICE = "INSERT INTO service " +
            "(service_name, service_description) " +
            "VALUES (?, ?);";*/

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    public ServiceDAOImpl() {}

    @Override
    public Optional<ServiceDescr> find(Number id) {
        logger.debug("Getting a service with id = " + id);
        Optional<ServiceDescr> serviceOpt = super.find(id);

        if(serviceOpt.isPresent()){
            return serviceOpt;
        }
        return Optional.empty();
    }

    @Override
    public void save(ServiceDescr service) {
        logger.debug("Saving a new service");
        super.save(service);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting service with id = " + id);
        getJdbcTemplate().update(DELETE_SERVICE, id);
    }

    @Override
    public void update(ServiceDescr service) {
        logger.debug("Updating service with id = " + service.getServiceId());
        super.update(service);
    }

    @Override
    public Optional<Long> findIdByName(String name, Number id){
        logger.debug("Getting a service with name = " + name);

        Long serviceId = getJdbcTemplate().queryForObject(FIND_SERVICE_BY_NAME, new Object[]{id, name}, Long.class);

        Optional<Long> serviceIdOpt = Optional.of(serviceId);

        return serviceIdOpt;
    }

    @Override
    public List<ServiceDescr> findAllByCarrierId(Number id){
        logger.debug("Getting services where carrierId = " + id);
        List<ServiceDescr> result = new ArrayList<>();

        result.addAll(getJdbcTemplate().query(FIND_ALL_SERVICES, new Object[]{id}, getGenericMapper()));
        return result;
    }

    @Override
    public List<ServiceDescr> findPaginByCarrierId(Number id, Integer from, Integer amount){
        logger.debug("Getting" + amount + "pagine services where carrierId = " + id);

        List<ServiceDescr> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_PAGIN_SERVICES, new Object[]{id}, getGenericMapper()));
        return result;
    }

}
