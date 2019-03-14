package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.model.Planet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PlanetDAOImpl extends CrudDAOImpl<Planet> implements PlanetDAO {

    private final String FIND_ALL_PLANETS_SQL = "SELECT planet_id, planet_name FROM planet";
    private final String FIND_ID_BY_PlANET_NAME = "SELECT planet_id FROM planet WHERE planet_name = UPPER(?)";

    @Override
    public List<Planet> findAllPlanets() {

        List<Planet> planets = getJdbcTemplate().query(FIND_ALL_PLANETS_SQL,
                new BeanPropertyRowMapper(Planet.class));

        return planets;
    }

    @Override
    public Long getIdByPlanetName(String planet) {
        return getJdbcTemplate().queryForObject(FIND_ID_BY_PlANET_NAME, new Object[]{planet}, Long.class);
    }
}
