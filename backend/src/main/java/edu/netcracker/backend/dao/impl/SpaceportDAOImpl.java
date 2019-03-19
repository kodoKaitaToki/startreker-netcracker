package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.model.Spaceport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpaceportDAOImpl extends CrudDAOImpl<Spaceport> implements SpaceportDAO {

    private final String findPerPeriod = "SELECT * FROM spaceport WHERE creation_date BETWEEN ? AND ?";

    private final String FIND_SPACEPORTS_OF_PLANET = "SELECT spaceport_id, spaceport_name, creation_date, planet_id\n" +
            "FROM spaceport\n" +
            "WHERE planet_id = ?";

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    @Override
    public List<Spaceport> findPerPeriod(LocalDateTime from, LocalDateTime to) {
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(
                findPerPeriod,
                new Object[]{from, to},
                getGenericMapper()));

        return spaceports;
    }

    @Override
    public List<Spaceport> findSpaceportsOfPlanet(Integer planetId){
        logger.debug("Querying spaceports for planet_id={}", planetId);
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(
                FIND_SPACEPORTS_OF_PLANET,
                new Object[]{planetId},
                getGenericMapper()));

        return spaceports;
    }
}
