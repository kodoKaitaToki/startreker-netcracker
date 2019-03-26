package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.model.Planet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:sql/planetdao.properties")
public class PlanetDAOImpl extends CrudDAOImpl<Planet> implements PlanetDAO {

    @Value("${FIND_ID_BY_PlANET_NAME}")
    private String FIND_ID_BY_PlANET_NAME;

    @Value("${FIND_ALL_PLANETS}")
    private String FIND_ALL_PLANETS;

    private static final Logger logger = LoggerFactory.getLogger(PlanetDAOImpl.class);

    @Override
    public Long getIdByPlanetName(String planet) {
        logger.debug("Getting id of {}", planet);
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
