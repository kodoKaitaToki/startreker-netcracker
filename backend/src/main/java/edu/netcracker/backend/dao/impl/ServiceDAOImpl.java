package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.history.HistoryServiceMapper;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.history.HistoryService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

@Repository
public class ServiceDAOImpl extends CrudDAOImpl<ServiceDescr> implements ServiceDAO {

    private final String FIND_SERVICE_BY_NAME = "SELECT * "
                                                + "FROM service "
                                                + "WHERE carrier_id = ? "
                                                + "AND service_name = ?";

    private final String FIND_ALL_SERVICES = "SELECT service.service_id, "
                                             + "        service.carrier_id, "
                                             + "        user_a.user_name,"
                                             + "        service.service_name, "
                                             + "        service.service_description, "
                                             + "        service.service_status, "
                                             + "        service.creation_date "
                                             + "FROM service"
                                             + "LEFT JOIN user_a "
                                             + "ON service.approver_id = user_a.user_id "
                                             + "WHERE carrier_id = ?"
                                             + "ORDER BY service_id";

    private final String FIND_PAGIN_SERVICES = "SELECT service.service_id, "
                                               + "        service.carrier_id, "
                                               + "        user_a.user_name, "
                                               + "        service.service_name,"
                                               + "        service.service_description, "
                                               + "        service.service_status, "
                                               + "        service.creation_date "
                                               + "FROM service "
                                               + "LEFT JOIN user_a "
                                               + "ON service.approver_id = user_a.user_id "
                                               + "WHERE carrier_id = ? "
                                               + "ORDER BY service_id "
                                               + "LIMIT ? OFFSET ?";

    private final String DELETE_SERVICE = "DELETE FROM service " + "WHERE service_id = ?";

    private final String FIND_BY_STATUS = "SELECT service.service_id, "
                                          + "        service.carrier_id, "
                                          + "        user_a.user_name, "
                                          + "        service.service_name, "
                                          + "        service.service_description, "
                                          + "        service.service_status, "
                                          + "        service.creation_date "
                                          + "FROM service "
                                          + "LEFT JOIN user_a "
                                          + "ON service.approver_id = user_a.user_id "
                                          + "WHERE carrier_id = ? "
                                          + "AND service_status = ? "
                                          + "ORDER BY service_id";

    private final String APPROVER_FIND_BY_STATUS = "SELECT service.service_id, "
                                                   + "        service.carrier_id, "
                                                   + "        user_a.user_name, "
                                                   + "        service.service_name, "
                                                   + "        service.service_description, "
                                                   + "        service.service_status, "
                                                   + "        service.creation_date "
                                                   + "FROM service "
                                                   + "LEFT JOIN user_a "
                                                   + "ON service.approver_id = user_a.user_id "
                                                   + "WHERE service_status = ? "
                                                   + "ORDER BY service_id "
                                                   + "LIMIT ? OFFSET ?";

    private final String APPROVER_FIND_BY_STATUS_AND_ID = "SELECT service.service_id, "
                                                          + "        service.carrier_id, "
                                                          + "        user_a.user_name, "
                                                          + "        service.service_name, "
                                                          + "        service.service_description, "
                                                          + "        service.service_status, "
                                                          + "        service.creation_date "
                                                          + "FROM service "
                                                          + "LEFT JOIN user_a "
                                                          + "ON service.approver_id = user_a.user_id "
                                                          + "WHERE approver_id = ? "
                                                          + "AND service_status = ? "
                                                          + "ORDER BY service_id "
                                                          + "LIMIT ? OFFSET ?";

    private final String FIND_ALL_REPLY_TEXTS = "SELECT reply_text "
                                                + "FROM service_reply "
                                                + "WHERE service_id = ? "
                                                + "AND creation_date = (SELECT MAX(creation_date) "
                                                + "                    FROM service_reply "
                                                + "                    WHERE service_id = ?)";

    private final static String GET_ALL_SERVICES_BELONG_TO_SUGGESTIONS = "SELECT "
                                                                         + "  s_service.suggestion_id AS suggestion_id, "
                                                                         + "  service.service_id AS servicedescr_service_id, "
                                                                         + "  service.service_name AS servicedescr_service_name "
                                                                         + "FROM service "
                                                                         + "INNER JOIN possible_service p_service ON p_service.service_id = service.service_id "
                                                                         + "INNER JOIN suggested_service s_service ON s_service.p_service_id = p_service.p_service_id "
                                                                         + "WHERE s_service.suggestion_id IN (:suggestionIds)";

    private final static String GET_SERVICE_NAMES_BY_TICKET = "SELECT s.service_name, count(s.service_id) "
                                                              + "FROM ticket t "
                                                              + "JOIN bought_service bs ON t.ticket_id=bs.ticket_id "
                                                              + "AND t.ticket_id = ? "
                                                              + "JOIN possible_service ps ON bs.p_service_id=ps.p_service_id "
                                                              + "JOIN service s ON ps.service_id = s.service_id "
                                                              + "GROUP BY s.service_id "
                                                              + "ORDER BY s.service_id";

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    private ServiceMapper mapper = new ServiceMapper();


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<ServiceDescr> find(Number id) {
        logger.debug("Getting a service with id = " + id);
        Optional<ServiceDescr> serviceOpt = super.find(id);

        if (serviceOpt.isPresent()) {
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
    public Optional<ServiceDescr> findByName(String name, Number id) {
        logger.debug("Getting a service with name = " + name);

        try {
            ServiceDescr serviceOpt = getJdbcTemplate().queryForObject(FIND_SERVICE_BY_NAME,
                                                                       new Object[]{id, name},
                                                                       getGenericMapper());
            return serviceOpt != null ? Optional.of(serviceOpt) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<ServiceCRUDDTO> findAllByCarrierId(Number id) {
        logger.debug("Getting services where carrierId = " + id);
        List<ServiceCRUDDTO> result = new ArrayList<>();

        result.addAll(getJdbcTemplate().query(FIND_ALL_SERVICES, new Object[]{id}, mapper));
        return result;
    }

    @Override
    public List<ServiceCRUDDTO> findPaginByCarrierId(Number id, Integer from, Integer amount) {
        logger.debug("Getting" + amount + "pagine services where carrierId = " + id);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_PAGIN_SERVICES, new Object[]{id, amount, from}, mapper));
        result.forEach(this::attachReply);

        return result;
    }

    @Override
    public List<ServiceCRUDDTO> findByStatus(Number id, Integer status) {
        logger.debug("Getting services where status = " + status);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(FIND_BY_STATUS, new Object[]{id, status}, mapper));
        result.forEach(this::attachReply);

        return result;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, Integer status) {
        logger.debug("Pagin services where status = " + status);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(APPROVER_FIND_BY_STATUS, new Object[]{status, number, from}, mapper));
        return result;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from,
                                                       Integer number,
                                                       Integer status,
                                                       Integer approverId) {
        logger.debug("Pagin services where status = " + status + " and approver = " + approverId);

        List<ServiceCRUDDTO> result = new ArrayList<>();
        result.addAll(getJdbcTemplate().query(APPROVER_FIND_BY_STATUS_AND_ID,
                                              new Object[]{approverId, status, number, from},
                                              mapper));
        return result;
    }

    private ServiceCRUDDTO attachReply(ServiceCRUDDTO serviceCRUDDTO) {
        Long id = serviceCRUDDTO.getId();
        try {
            String replyText = getJdbcTemplate().queryForObject(FIND_ALL_REPLY_TEXTS,
                                                                new Object[]{id, id},
                                                                String.class);
            serviceCRUDDTO.setReplyText(replyText);
            return serviceCRUDDTO;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
        logger.info("Querying purchased services for ticket {}", id);
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
