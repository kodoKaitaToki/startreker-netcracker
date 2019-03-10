package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

@Repository
public class ServiceDAOImpl extends CrudDAOImpl<ServiceDescr> implements ServiceDAO {

    private final String FIND_SERVICE_BY_NAME = "SELECT * \n" +
            "FROM service\n" +
            "WHERE carrier_id = ?\n" +
            "AND service_name = ?";

    private final String DELETE_SERVICE = "DELETE FROM service\n" +
            "WHERE service_id = ?";

    private final String FIND_BY_STATUS = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "WHERE carrier_id = ?\n" +
            "AND service_status = ?\n" +
            "ORDER BY service_id";

    private final String APPROVER_FIND_BY_STATUS = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n"+
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "WHERE service_status = ?\n" +
            "ORDER BY service_id\n" +
            "LIMIT ? OFFSET ?";

    private final String APPROVER_FIND_BY_STATUS_AND_ID = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "WHERE approver_id = ?\n" +
            "AND service_status = ?\n" +
            "ORDER BY service_id\n" +
            "LIMIT ? OFFSET ?";

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    private ServiceMapper mapper = new ServiceMapper();

    public ServiceDAOImpl() {}

    @Override
    public Optional<ServiceDescr> find(Number id) {
        logger.debug("Querying a service by id = {}", id);
        Optional<ServiceDescr> serviceOpt = super.find(id);

        if(serviceOpt.isPresent()){
            return serviceOpt;
        }

        logger.debug("There is no service with id = {}", id);
        return Optional.empty();
    }

    @Override
    public void save(ServiceDescr service) {
        logger.debug("Saving a new service");
        super.save(service);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting a service with id = {}", id);
        getJdbcTemplate().update(DELETE_SERVICE, id);
    }

    @Override
    public void update(ServiceDescr service) {
        logger.debug("Updating service with id = {}", service.getServiceId());
        super.update(service);
    }

    @Override
    public Optional<ServiceDescr> findByName(String name, Number id){
        logger.debug("Querying a service by name = {} of carrier  = {}", name, id);
        Optional<ServiceDescr> serviceOpt =
                Optional.of(
                        getJdbcTemplate().queryForObject(FIND_SERVICE_BY_NAME, new Object[]{id, name}, getGenericMapper()));

        if(serviceOpt.isPresent()){
            return serviceOpt;
        }

        logger.debug("Carrier {} doesn't have service with name = {}", id, name);
        return Optional.empty();

    }

    @Override
    public List<ServiceCRUDDTO> findByStatus(Number id, Integer status){
        logger.debug("Querying services with status = {} of carrier = {}", status, id);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_BY_STATUS, new Object[]{id, status}, mapper));

        ServiceReplyDAO serviceReplyDAO = new ServiceReplyDAOImpl();

        result.forEach(service -> {
            Long serviceId = service.getId();
            String replyText = serviceReplyDAO.getLastReply(serviceId).orElse(null);
            service.setReplyText(replyText);
        });

        return result;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, Integer status) {
        logger.debug("Pagin services where status = " + status);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(
                getJdbcTemplate()
                        .query(
                                APPROVER_FIND_BY_STATUS,
                                new Object[]{status, number, from},
                                mapper));
        return result;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, Integer status, Integer approverId) {
        logger.debug("Pagin services where status = " + status + " and approver = " + approverId);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(APPROVER_FIND_BY_STATUS_AND_ID, new Object[]{approverId, status, number, from}, mapper));
        return result;
    }
}
