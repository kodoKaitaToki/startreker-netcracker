package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Planet;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Optional;

@PropertySource("classpath:sql/planetdao.properties")
public interface PlanetDAO {
    void save(Planet planet);

    Optional<Planet> find(Number id);

    List<Planet> getAllPlanets();

    Long getIdByPlanetName(String planet);

    void delete(Planet planet);

}
