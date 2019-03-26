package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.PossibleServiceMapper;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "log")
@Repository
@PropertySource("classpath:sql/possibleservicedao.properties")
public class PossibleServiceDAOImpl extends CrudDAOImpl<PossibleService> implements PossibleServiceDAO {

    private ServiceDAO serviceDAO;
    private final PossibleServiceMapper possibleServiceMapper;

    @Value("${FIND_ALL_WITH_CLASS_ID}")
    private String FIND_ALL_WITH_CLASS_ID;

    @Value("${FIND_ALL_P_SERVICES_BY_SUGGESTION_ID}")
    private String FIND_ALL_P_SERVICES_BY_SUGGESTION_ID;

    @Value("${BUY_P_SERVICE}")
    private String BUY_P_SERVICE;

    @Value("${SELECT_POSSIBLE_SERVICES_BY_CARRIER}")
    private String SELECT_POSSIBLE_SERVICES_BY_CARRIER;

    @Autowired
    public PossibleServiceDAOImpl(ServiceDAO serviceDAO, PossibleServiceMapper possibleServiceMapper) {
        this.serviceDAO = serviceDAO;
        this.possibleServiceMapper = possibleServiceMapper;
    }

    @Override
    public Optional<PossibleService> find(Number id) {
        log.debug("Getting possibleService by id {}", id);

        Optional<PossibleService> optPossibleService = super.find(id);

        if (!optPossibleService.isPresent()) {
            log.error("Possible service with id {} not found", id);
            return Optional.empty();
        }

        PossibleService possibleService = optPossibleService.get();
        Optional<ServiceDescr> attachedService = findService(possibleService);

        attachedService.ifPresent(possibleService::setService);

        return Optional.of(possibleService);
    }

    @Override
    public void delete(PossibleService possibleService) {
        log.debug("Deleting possibleService with id {}", possibleService.getPServiceId());
        possibleService.setPServiceStatus(2L);
        update(possibleService);
    }

    @Override
    public List<PossibleService> findAllWithClassId(Number id) {
        log.debug("Getting all possible services with id {}", id);

        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(FIND_ALL_WITH_CLASS_ID, new Object[]{id}, getGenericMapper()));

        possibleServices.forEach(possibleService -> findService(possibleService).ifPresent(possibleService::setService));

        return possibleServices;
    }

    private Optional<ServiceDescr> findService(PossibleService possibleService) {
        log.debug("Getting service by possible service with id {}", possibleService.getPServiceId());

        return serviceDAO.find(possibleService.getServiceId());
    }

    @Override
    public List<PossibleService> findAllPossibleServicesBySuggestionId(Number suggestionId) {
        log.debug("Getting all possible services by suggestion id {}", suggestionId);

        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(FIND_ALL_P_SERVICES_BY_SUGGESTION_ID,
                                                        new Object[]{suggestionId},
                                                        getGenericMapper()));

        return possibleServices;
    }

    @Override
    public void buyService(Ticket ticket, PossibleService possibleService) {
        log.debug("Buying possible service (id = {}) by ticket (id = {})",
                  possibleService.getPServiceId(),
                  ticket.getTicketId());

        getJdbcTemplate().update(BUY_P_SERVICE, possibleService.getPServiceId(), ticket.getTicketId());
    }

    @Override
    public List<PossibleService> findAllPossibleServicesByCarrier(Integer id) {
        return getJdbcTemplate().query(SELECT_POSSIBLE_SERVICES_BY_CARRIER, new Object[]{id}, possibleServiceMapper);
    }
}
