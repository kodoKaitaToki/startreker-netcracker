package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.ServiceDescr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PossibleServiceDAOImpl extends CrudDAOImpl<PossibleService> implements PossibleServiceDAO {

    private ServiceDAO serviceDAO;

    private String FIND_ALL_WITH_CLASS_ID = "SELECT * FROM possible_service " +
            "WHERE class_id = ?";

    private String FIND_ALL_P_SERVICES_BY_SUGGESTION_ID = "SELECT * FROM possible_service " +
            "INNER JOIN suggested_service ON possible_service.p_service_id = suggested_service.p_service_id " +
            "WHERE suggestion_id = ?";

    @Autowired
    public PossibleServiceDAOImpl(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    @Override
    public Optional<PossibleService> find(Number id) {
        Optional<PossibleService> optPossibleService = super.find(id);

        if (!optPossibleService.isPresent())
            return Optional.empty();

        PossibleService possibleService = optPossibleService.get();
        Optional<ServiceDescr> attachedService = findService(possibleService);

        attachedService.ifPresent(possibleService::setService);

        return Optional.of(possibleService);
    }

    @Override
    public List<PossibleService> findAllWithClassId(Number id) {
        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(
                FIND_ALL_WITH_CLASS_ID,
                new Object[]{id},
                getGenericMapper()));

        possibleServices.forEach(possibleService
                -> findService(possibleService).ifPresent(possibleService::setService));

        return possibleServices;
    }

    private Optional<ServiceDescr> findService(PossibleService possibleService) {
        return serviceDAO.find(possibleService.getServiceId());
    }

    @Override
    public List<PossibleService> findAllPossibleServicesBySuggestionId(Number suggestionId) {
        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(
                FIND_ALL_P_SERVICES_BY_SUGGESTION_ID,
                new Object[]{suggestionId},
                getGenericMapper()));

        return possibleServices;
    }
}
