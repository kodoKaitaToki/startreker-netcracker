package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.mapper.PossibleServiceMapper;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sql/possibleservicedao.properties")
public class PossibleServiceDAOImpl extends CrudDAOImpl<PossibleService> implements PossibleServiceDAO {

    private ServiceDAO serviceDAO;
    private final PossibleServiceMapper possibleServiceMapper;

    private String FIND_ALL_WITH_CLASS_ID = "SELECT p_service_id, "
                                            + "service_id, "
                                            + "class_id, "
                                            + "service_price, "
                                            + "p_service_status "
                                            + "FROM possible_service WHERE class_id = ? AND p_service_status = 1";

    private String FIND_ALL_P_SERVICES_BY_SUGGESTION_ID = "SELECT possible_service.p_service_id, "
                                                          + "service_id, "
                                                          + "class_id, "
                                                          + "service_price, "
                                                          + "p_service_status FROM possible_service "
                                                          + "INNER JOIN suggested_service ON possible_service.p_service_id = suggested_service.p_service_id "
                                                          + "WHERE suggestion_id = ? AND p_service_status = 1";

    private static final String BUY_P_SERVICE =
            "INSERT INTO bought_service (" + "p_service_id, " + "ticket_id) " + "VALUES (?, ?)";

    @Value("${SELECT_POSSIBLE_SERVICES_BY_CARRIER}")
    private String SELECT_POSSIBLE_SERVICES_BY_CARRIER;

    @Autowired
    public PossibleServiceDAOImpl(ServiceDAO serviceDAO, PossibleServiceMapper possibleServiceMapper) {
        this.serviceDAO = serviceDAO;
        this.possibleServiceMapper = possibleServiceMapper;
    }

    @Override
    public Optional<PossibleService> find(Number id) {
        Optional<PossibleService> optPossibleService = super.find(id);

        if (!optPossibleService.isPresent()) {
            return Optional.empty();
        }

        PossibleService possibleService = optPossibleService.get();
        Optional<ServiceDescr> attachedService = findService(possibleService);

        attachedService.ifPresent(possibleService::setService);

        return Optional.of(possibleService);
    }

    @Override
    public void delete(PossibleService possibleService) {
        possibleService.setPServiceStatus(2L);
        update(possibleService);
    }

    @Override
    public List<PossibleService> findAllWithClassId(Number id) {
        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(FIND_ALL_WITH_CLASS_ID, new Object[]{id}, getGenericMapper()));

        possibleServices.forEach(possibleService -> findService(possibleService).ifPresent(possibleService::setService));

        return possibleServices;
    }

    private Optional<ServiceDescr> findService(PossibleService possibleService) {
        return serviceDAO.find(possibleService.getServiceId());
    }

    @Override
    public List<PossibleService> findAllPossibleServicesBySuggestionId(Number suggestionId) {
        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(FIND_ALL_P_SERVICES_BY_SUGGESTION_ID,
                                                        new Object[]{suggestionId},
                                                        getGenericMapper()));

        return possibleServices;
    }

    @Override
    public void buyService(Ticket ticket, PossibleService possibleService) {
        getJdbcTemplate().update(BUY_P_SERVICE, possibleService.getPServiceId(), ticket.getTicketId());
    }

    @Override
    public List<PossibleService> findAllPossibleServicesByCarrier(Integer id) {
        return getJdbcTemplate().query(SELECT_POSSIBLE_SERVICES_BY_CARRIER, new Object[]{id}, possibleServiceMapper);
    }
}
