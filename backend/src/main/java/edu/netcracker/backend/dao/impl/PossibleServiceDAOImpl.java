package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Service;
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

    @Autowired
    public PossibleServiceDAOImpl(ServiceDAO serviceDAO) {
        serviceDAO = serviceDAO;
    }

    @Override
    public List<PossibleService> findAllWithClassId(Number id) {
        List<PossibleService> possibleServices = new ArrayList<>();

        possibleServices.addAll(getJdbcTemplate().query(
                FIND_ALL_WITH_CLASS_ID,
                new Object[]{id},
                getGenericMapper()));

        return possibleServices;
    }

    @Override
    public Optional<Service> findService(PossibleService possibleService) {
        return serviceDAO.find(possibleService.getServiceId());
    }
}
