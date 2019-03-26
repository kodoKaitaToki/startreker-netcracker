package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.model.Planet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlanetDAOImpl extends CrudDAOImpl<Planet> implements PlanetDAO {

    private final String FIND_ID_BY_PlANET_NAME = "SELECT planet_id FROM planet WHERE planet_name = UPPER(?)";
    private final String FIND_ALL_PLANETS = "SELECT planet_id, planet_name FROM planet ORDER BY planet_name";
    private static final Logger logger = LoggerFactory.getLogger(ServiceDAOImpl.class);

    @Override
    public List<Planet> findAllPlanets() {
        logger.debug("Getting all planets");

        return getJdbcTemplate().query(FIND_ALL_PLANETS,
                new BeanPropertyRowMapper(Planet.class));
    }

    @Override
    public Long getIdByPlanetName(String planet) {
        logger.debug("Getting planet by name {}", planet);

        return getJdbcTemplate().queryForObject(FIND_ID_BY_PlANET_NAME, new Object[]{planet}, Long.class);
    }

    @Override
    public List<Planet> getAllPlanets() {
        logger.debug("Querying all planets");
        List<Planet> planets = new ArrayList<>();
        planets.addAll(getJdbcTemplate().query(FIND_ALL_PLANETS, getGenericMapper()));
        return planets;
    }
}
