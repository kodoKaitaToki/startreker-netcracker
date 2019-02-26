package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.PossibleServiceMapper;
import edu.netcracker.backend.dao.mapper.ServiceSuggestionMapper;
import edu.netcracker.backend.model.Service;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceDAOImpl extends CrudDAO<Service> implements ServiceDAO {

    private RowMapper possibleServiceMapper = new PossibleServiceMapper();
    private RowMapper serviceSuggestionMapper = new ServiceSuggestionMapper();

    private final String FIND_ALL = "SELECT * FROM service";

    private final String FIND_ALL_WITH_TICKET_CLASS_ID = "SELECT * FROM service " +
            "INNER JOIN possible_service ps on service.service_id = ps.service_id " +
            "WHERE ps.class_id = ?";

    private final String FIND_SUGGESTIONS_WITH_TICKET_CLASS_ID = "SELECT * FROM service " +
            "INNER JOIN possible_service ps on service.service_id = ps.service_id " +
            "INNER JOIN class_suggestion cs on ps.p_service_id = cs.slaveclass_id " +
            "WHERE cs.ownerclass_id = ?";

    private final String SAVE_SUGGESTION = "INSERT into class_suggestion (ownerclass_id, slaveclass_id, discount_rate) VALUES (?, ?, ?)";

    private final String DELETE_SUGGESTION = "DELETE FROM class_suggestion WHERE ownerclass_id = ? AND slaveclass_id = ?";

    private final String FIND_SUGGESTION = "SELECT * FROM service " +
            "INNER JOIN possible_service ps on service.service_id = ps.service_id " +
            "INNER JOIN class_suggestion cs on ps.p_service_id = cs.slaveclass_id " +
            "WHERE cs.ownerclass_id = ? AND cs.slaveclass_id = ?";

    @Override
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();

        services.addAll(getJdbcTemplate().query(
                FIND_ALL,
                getGenericMapper()));

        return services;
    }

    @Override
    public List<Service> findAllWithTicketClassId(Number id) {
        List<Service> services = new ArrayList<>();

        services.addAll(getJdbcTemplate().query(
                FIND_ALL_WITH_TICKET_CLASS_ID,
                new Object[]{id},
                possibleServiceMapper));

        return services;
    }

    @Override
    public List<Service> findSuggestionsWithTicketClassId(Number id) {
        List<Service> services = new ArrayList<>();

        services.addAll(getJdbcTemplate().query(
                FIND_SUGGESTIONS_WITH_TICKET_CLASS_ID,
                new Object[]{id},
                serviceSuggestionMapper));

        return services;
    }

    @Override
    public Optional<Service> findSuggestion(Number tripId, Number serviceId) {
        try {
            return Optional.of((Service) getJdbcTemplate().queryForObject(
                    FIND_SUGGESTION,
                    new Object[]{serviceId, tripId},
                    serviceSuggestionMapper
            ));
        }
        catch (EmptyResultDataAccessException e) {
            System.out.println("err");
            return Optional.empty();
        }
    }

    @Override
    public void saveSuggestion(Number tripId,
                               Number serviceId,
                               Number discount_rate) {
        System.out.println(tripId + " ? " + serviceId);

        if (findSuggestion(tripId, serviceId).isPresent())
            return;

        System.out.println(tripId + " ! " + serviceId);
        getJdbcTemplate().update(SAVE_SUGGESTION, tripId, serviceId, discount_rate);
    }

    @Override
    public void deleteSuggestion(Number tripId,
                                 Number serviceId) {
        getJdbcTemplate().update(DELETE_SUGGESTION, tripId, serviceId);
    }
}
