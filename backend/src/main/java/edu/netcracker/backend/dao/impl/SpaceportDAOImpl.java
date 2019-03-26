package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.model.Spaceport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpaceportDAOImpl extends CrudDAOImpl<Spaceport> implements SpaceportDAO {

    private final String FIND_BY_PERIOD_SQL = "SELECT * FROM spaceport WHERE creation_date BETWEEN ? AND ?";
    private final String FIND_BY_PLANET_SQL = "SELECT * FROM spaceport AS s "
                                              + "INNER JOIN planet AS p ON s.planet_id = p.planet_id "
                                              + "WHERE planet_name = ?";
    private final String FIND_ID_BY_SPACEPORT_NAME
            = "SELECT spaceport_id FROM spaceport WHERE spaceport_name = LOWER(?) AND planet_id = ?";

    private final String FIND_SPACEPORTS_OF_PLANET = "SELECT spaceport_id, spaceport_name, creation_date, planet_id\n"
                                                     + "FROM spaceport\n"
                                                     + "WHERE planet_id = ?\n"
                                                     + "ORDER BY spaceport_name";

    @Override
    public List<Spaceport> findByPlanet(String planet) {
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(FIND_BY_PLANET_SQL, new Object[]{planet}, getGenericMapper()));

        return spaceports;
    }

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    @Override
    public List<Spaceport> findPerPeriod(LocalDateTime from, LocalDateTime to) {
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(FIND_BY_PERIOD_SQL, new Object[]{from, to}, getGenericMapper()));

        return spaceports;
    }

    @Override
    public Long getIdBySpaceportName(String spaceport, Long planetId) {
        return getJdbcTemplate().queryForObject(FIND_ID_BY_SPACEPORT_NAME,
                                                new Object[]{spaceport, planetId},
                                                Long.class);
    }

    public List<Spaceport> findSpaceportsOfPlanet(Integer planetId) {
        logger.debug("Querying spaceports for planet_id={}", planetId);
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(FIND_SPACEPORTS_OF_PLANET,
                                                  new Object[]{planetId},
                                                  getGenericMapper()));

        return spaceports;
    }
}
