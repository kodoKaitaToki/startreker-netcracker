package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.history.HistoryServiceMapper;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.history.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

@Repository
@Slf4j(topic = "log")
@PropertySource("classpath:sql/servicedao.properties")
public class ServiceDAOImpl extends CrudDAOImpl<ServiceDescr> implements ServiceDAO {

    @Value("${FIND_SERVICE_BY_NAME}")
    private String FIND_SERVICE_BY_NAME;

    @Value("${FIND_PAGIN_SERVICES}")
    private String FIND_PAGIN_SERVICES;

    @Value("${APPROVER_FIND_BY_STATUS}")
    private String APPROVER_FIND_BY_STATUS;

    @Value("${APPROVER_FIND_BY_STATUS_AND_ID}")
    private String APPROVER_FIND_BY_STATUS_AND_ID;

    @Value("${GET_ALL_SERVICES_BELONG_TO_SUGGESTIONS}")
    private String GET_ALL_SERVICES_BELONG_TO_SUGGESTIONS;

    @Value("${GET_SERVICE_NAMES_BY_TICKET}")
    private String GET_SERVICE_NAMES_BY_TICKET;

    private ServiceMapper mapper = new ServiceMapper();

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<ServiceDescr> find(Number id) {
        log.debug("ServiceDAO.find(Number id) was invoked with parameters id={}", id);
        Optional<ServiceDescr> serviceOpt = super.find(id);

        if (serviceOpt.isPresent()) {
            return serviceOpt;
        }
        return Optional.empty();
    }

    @Override
    public void save(ServiceDescr service) {
        log.debug("ServiceDAO.save(ServiceDescr service) was invoked with parameters service={}", service);
        super.save(service);
    }

    @Override
    public void update(ServiceDescr service) {
        log.debug("ServiceDAO.update(ServiceDescr service) was invoked with parameters service={}", service);
        super.update(service);
    }

    @Override
    public Optional<ServiceDescr> findByName(String name, Number id) {
        log.debug("ServiceDAO.findByName(String name, Number id) was invoked with parameters name={}, id={}", name, id);

        try {
            return Optional.of(getJdbcTemplate().queryForObject(FIND_SERVICE_BY_NAME,
                                                                new Object[]{id, name},
                                                                getGenericMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<ServiceCRUDDTO> findByCarrierId(Number id, Integer offset, Integer limit, Integer status) {
        log.debug("ServiceDAO.findPaginByCarrierId(Number id, Integer status) was invoked with parameters id={}, " +
                "offset={}, limit={}, status={}", id, offset, limit, status);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_PAGIN_SERVICES, new Object[]{id ,status, limit, offset}, mapper));

        return result;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, Integer status) {
        log.debug("Pagin services where status = " + status);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(APPROVER_FIND_BY_STATUS, new Object[]{status, number, from}, mapper));
        return result;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from,
                                                       Integer number,
                                                       Integer status,
                                                       Integer approverId) {
        log.debug("Pagin services where status = " + status + " and approver = " + approverId);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(APPROVER_FIND_BY_STATUS_AND_ID,
                                              new Object[]{approverId, status, number, from},
                                              mapper));
        return result;
    }

    @Override
    public Map<Long, List<ServiceDescr>> getAllServicesBelongToSuggestions(List<Number> suggestionIds) {
        Map<Long, List<ServiceDescr>> relatedServices = new HashMap<>();

        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(GET_ALL_SERVICES_BELONG_TO_SUGGESTIONS,
                                                                                 new MapSqlParameterSource(
                                                                                         "suggestionIds",
                                                                                         suggestionIds));
        for (Map<String, Object> row : rows) {
            List<ServiceDescr> ticketClasses
                    = relatedServices.computeIfAbsent(((Number) row.get("suggestion_id")).longValue(),
                                                      aLong -> new ArrayList<>());

            ticketClasses.add(createService(row));
        }

        return relatedServices;
    }

    @Override
    public List<HistoryService> getServiceNamesByTicket(Number id) {
        log.info("Querying purchased services for ticket {}", id);
        return getJdbcTemplate().query(GET_SERVICE_NAMES_BY_TICKET, new Object[]{id}, new HistoryServiceMapper());
    }

    private ServiceDescr createService(Map<String, Object> row) {
        return ServiceDescr.builder()
                           .serviceId(((Number) row.get("servicedescr_service_id")).longValue())
                           .serviceName((String) row.get("servicedescr_service_name"))
                           .build();
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
