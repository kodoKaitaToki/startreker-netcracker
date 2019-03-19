package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.model.Planet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlanetDAOImpl extends CrudDAOImpl<Planet> implements PlanetDAO {

    private final String FIND_ALL_PLANETS = "SELECT planet_id, planet_name\n" +
            "FROM planet";

    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    @Override
    public List<Planet> getAllPlanets() {
        logger.debug("Querying all planets");
        List<Planet> planets = new ArrayList<>();

        planets.addAll(getJdbcTemplate().query(FIND_ALL_PLANETS, getGenericMapper()));
        return planets;
    }
}
