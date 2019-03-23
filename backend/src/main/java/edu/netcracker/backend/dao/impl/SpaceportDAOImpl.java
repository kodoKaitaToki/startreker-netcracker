package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.model.Spaceport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:sql/spaceportdao.properties")
public class SpaceportDAOImpl extends CrudDAOImpl<Spaceport> implements SpaceportDAO {

    @Value("${FIND_BY_PERIOD_SQL}")
    private String FIND_BY_PERIOD_SQL;

    @Value("${FIND_BY_PLANET_NAME}")
    private String FIND_BY_PLANET_NAME;

    @Value("${FIND_BY_PLANET_ID}")
    private String FIND_BY_PLANET_ID;

    @Value("${FIND_ID_BY_SPACEPORT_NAME}")
    private String FIND_ID_BY_SPACEPORT_NAME;

    private static final Logger logger = LoggerFactory.getLogger(SpaceportDAOImpl.class);

    @Override
    public List<Spaceport> findByPlanet(String planet) {
        List<Spaceport> spaceports = new ArrayList<>();
        logger.debug("Querying all spaceports of {}", planet);
        spaceports.addAll(getJdbcTemplate().query(FIND_BY_PLANET_NAME, new Object[]{planet}, getGenericMapper()));

        return spaceports;
    }

    @Override
    public List<Spaceport> findPerPeriod(LocalDateTime from, LocalDateTime to) {
        List<Spaceport> spaceports = new ArrayList<>();
        logger.debug("Querying all spaceports created in period from {} to {}", from, to);
        spaceports.addAll(getJdbcTemplate().query(FIND_BY_PERIOD_SQL, new Object[]{from, to}, getGenericMapper()));

        return spaceports;
    }

    @Override
    public Long getIdBySpaceportName(String spaceport, Long planetId) {
        logger.debug("Getting id of {} spaceport situated on planet with id {}", spaceport, planetId);
        return getJdbcTemplate().queryForObject(FIND_ID_BY_SPACEPORT_NAME,
                                                new Object[]{spaceport, planetId},
                                                Long.class);
    }

    public List<Spaceport> findSpaceportsOfPlanet(Integer planetId) {
        logger.debug("Querying spaceports for planet_id={}", planetId);
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(FIND_BY_PLANET_ID, new Object[]{planetId}, getGenericMapper()));

        return spaceports;
    }
}
