package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.message.request.ServicePendingActivationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j(topic = "log")
@PropertySource("classpath:sql/pendingTripAndServicesDao.properties")
public class ServicePendingDao implements IPendingDao<ServicePendingActivationDto> {

    private JdbcTemplate jdbcTemplate;

    @Value("${GET_SERVICES_PENDING_ACTIVATION}")
    private String GET_SERVICES_PENDING_ACTIVATION;

    @Value("${LIMIT_AND_OFFSET}")
    private String LIMIT_AND_OFFSET;

    @Override
    public List<ServicePendingActivationDto> getPendingEntries() {

        log.debug("ServicePendingDao.getPendingEntries() was invoked");

        return jdbcTemplate.queryForList(GET_SERVICES_PENDING_ACTIVATION)
                .stream()
                .map(this::getPendingActivationService)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicePendingActivationDto> getPendingWithOffsetAndLimit(Number limit, Number offset) {

        log.debug("ServicePendingDao.getPendingWithOffsetAndLimit(Number limit, Number offset) was invoked with parameters limit = {} offset = {}", limit, offset);

        return jdbcTemplate.queryForList(GET_SERVICES_PENDING_ACTIVATION + LIMIT_AND_OFFSET, new Object[]{limit, offset})
                .stream()
                .map(this::getPendingActivationService)
                .collect(Collectors.toList());
    }

    private ServicePendingActivationDto getPendingActivationService(Map<String, Object> obj) {

        log.debug("ServicePendingDao.getPendingActivationService(Map<String, Object> obj) was invoked");

        return ServicePendingActivationDto.builder()
                .serviceId((Integer) obj.get("srvc_id"))
                .serviceName((String) obj.get("srvc_name"))
                .serviceDescr((String) obj.get("srvc_descr"))
                .serviceStatus((Integer) obj.get("srvc_status"))
                .creationDate(((Timestamp) obj.get("srvc_creation_date")).toLocalDateTime().toString())
                .approverName((String) obj.get("approver_name"))
                .approverEmail((String) obj.get("approver_email"))
                .approverTel((String) obj.get("approver_tel"))
                .carrierName((String) obj.get("carrier_name"))
                .carrierEmail((String) obj.get("carrier_email"))
                .carrierTel((String) obj.get("carrier_tel"))
                .build();
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
