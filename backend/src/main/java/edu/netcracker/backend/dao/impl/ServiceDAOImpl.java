package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceDAOImpl extends CrudDAOImpl<ServiceDescr> implements ServiceDAO {

    private final String FIND_SERVICE_BY_NAME = "SELECT * \n" +
            "FROM service\n" +
            "WHERE carrier_id = ?\n" +
            "AND service_name = ?";

    private final String FIND_ALL_SERVICES = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n" +
            "        service_reply.reply_text\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "LEFT JOIN service_reply\n" +
            "ON service.service_id = service_reply.service_id\n" +
            "WHERE carrier_id = ?\n" +
            "ORDER BY service_id";

    private final String FIND_PAGIN_SERVICES = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n" +
            "        service_reply.reply_text\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "LEFT JOIN service_reply\n" +
            "ON service.service_id = service_reply.service_id\n" +
            "WHERE carrier_id = ?\n" +
            "ORDER BY service_id\n" +
            "LIMIT ? OFFSET ?";

    private final String DELETE_SERVICE = "DELETE FROM service\n" +
            "WHERE service_id = ?";

    private final String FIND_BY_STATUS = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n" +
            "        service_reply.reply_text\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "LEFT JOIN service_reply\n" +
            "ON service.service_id = service_reply.service_id\n" +
            "WHERE carrier_id = ?\n" +
            "AND service_status = ?\n" +
            "ORDER BY service_id";

    private final String FIND_SERVICES_APPROVER = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n" +
            "        service_reply.reply_text\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "LEFT JOIN service_reply\n" +
            "ON service.service_id = service_reply.service_id\n" +
            "WHERE approver_id = ?\n" +
            "AND service_status = ?\n" +
            "ORDER BY service_id\n" +
            "LIMIT ? OFFSET ?";

    private final String APPROVER_FIND_BY_STATUS = "SELECT service.service_id,\n" +
            "        service.carrier_id,\n" +
            "        user_a.user_name,\n" +
            "        service.service_name,\n" +
            "        service.service_description,\n" +
            "        service.service_status,\n" +
            "        service.creation_date,\n" +
            "        service_reply.reply_text\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "LEFT JOIN service_reply\n" +
            "ON service.service_id = service_reply.service_id\n" +
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
            "        service_reply.reply_text\n" +
            "FROM service\n" +
            "LEFT JOIN user_a\n" +
            "ON service.approver_id = user_a.user_id\n" +
            "LEFT JOIN service_reply\n" +
            "ON service.service_id = service_reply.service_id\n" +
            "WHERE approver_id = ?\n" +
            "AND service_status = ?\n" +
            "ORDER BY service_id\n" +
            "LIMIT ? OFFSET ?";

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    private ServiceMapper mapper = new ServiceMapper();

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
    public Optional<ServiceDescr> findByName(String name, Number id){
        logger.debug("Getting a service with name = " + name);

        try {
            ServiceDescr serviceOpt =
                    getJdbcTemplate().queryForObject(FIND_SERVICE_BY_NAME, new Object[]{id, name}, getGenericMapper());
            return serviceOpt != null ? Optional.of(serviceOpt) : Optional.empty();
        }catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }

    @Override
    public List<ServiceDTO> findAllByCarrierId(Number id){
        logger.debug("Getting services where carrierId = " + id);
        List<ServiceDTO> result = new ArrayList<>();

        result.addAll(getJdbcTemplate().query(FIND_ALL_SERVICES, new Object[]{id}, mapper));
        return result;
    }

    @Override
    public List<ServiceDTO> findPaginByCarrierId(Number id, Integer from, Integer amount){
        logger.debug("Getting" + amount + "pagine services where carrierId = " + id);

        List<ServiceDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_PAGIN_SERVICES, new Object[]{id, amount, from}, mapper));
        return result;
    }

    @Override
    public List<ServiceDTO> findByStatus(Number id, Integer status){
        logger.debug("Getting services where status = " + status);

        List<ServiceDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_BY_STATUS, new Object[]{id, status}, mapper));
        return result;
    }

    @Override
    public List<ServiceDTO> getServicesForApprover(Integer from, Integer number, Integer status) {
        logger.debug("Pagin services where status = " + status);

        List<ServiceDTO> result = new ArrayList<>();
        result.addAll(
                getJdbcTemplate()
                        .query(
                                APPROVER_FIND_BY_STATUS,
                                new Object[]{status, number, from},
                                mapper));
        return result;
    }

    @Override
    public List<ServiceDTO> getServicesForApprover(Integer from, Integer number, Integer status, Integer approverId) {
        logger.debug("Pagin services where status = " + status + " and approver = " + approverId);

        List<ServiceDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(APPROVER_FIND_BY_STATUS_AND_ID, new Object[]{approverId, status, number, from}, mapper));
        return result;
    }

}
