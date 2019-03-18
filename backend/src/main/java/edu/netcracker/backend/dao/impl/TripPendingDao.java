package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.IPendingDao;
import edu.netcracker.backend.dao.sql.PendingActivationScripts;
import edu.netcracker.backend.message.request.PendingActivationTrip;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j(topic = "log")
public class TripPendingDao implements IPendingDao<PendingActivationTrip> {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PendingActivationTrip> getPendingEntries() {

        log.debug("TripPendingDao.getPendingEntries() was invoked");

        return jdbcTemplate.queryForList(PendingActivationScripts.GET_TRIPS_PENDING_ACTIVATION)
                .stream()
                .map(this::getPendingActivationTrip)
                .collect(Collectors.toList());
    }

    @Override
    public List<PendingActivationTrip> getPendingWithOffsetAndLimit(Number limit, Number offset) {

        log.debug("TripPendingDao.getPendingWithOffsetAndLimit(Number limit, Number offset) was invoked with parameters limit = {} offset = {}", limit, offset);

        return jdbcTemplate.queryForList(PendingActivationScripts.GET_TRIPS_PENDING_ACTIVATION + PendingActivationScripts.LIMIT_AND_OFFSET, new Object[]{limit, offset})
                .stream()
                .map(this::getPendingActivationTrip)
                .collect(Collectors.toList());
    }

    private PendingActivationTrip getPendingActivationTrip(Map<String, Object> obj) {

        log.debug("TripPendingDao.getPendingActivationTrip(Map<String, Object> obj) was invoked");

        return PendingActivationTrip.builder()
                .tripID((Integer) obj.get("trip_id"))
                .tripStatus(((Integer) obj.get("trip_status")).toString())
                .arrivalDate(((Timestamp) obj.get("arrival_date")).toLocalDateTime().toString())
                .departureDate(((Timestamp) obj.get("departure_date")).toLocalDateTime().toString())
                .creationDate(((Timestamp) obj.get("creation_date")).toLocalDateTime().toString())
                .approverName((String) obj.get("approver_name"))
                .approverEmail((String) obj.get("approver_email"))
                .approverTel((String) obj.get("approver_tel"))
                .carrierName((String) obj.get("carrier_name"))
                .carrierEmail((String) obj.get("carrier_email"))
                .carrierTel((String) obj.get("carrier_tel"))
                .departmentPlanetName((String) obj.get("planet_departure"))
                .departmentSpaceportName((String) obj.get("spaceport_departure"))
                .arrivalPlanetName((String) obj.get("planet_arrival"))
                .arrivalSpaceportName((String) obj.get("spaceport_arrival"))
                .build();
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
