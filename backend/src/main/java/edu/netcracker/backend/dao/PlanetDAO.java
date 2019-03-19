package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetDAO {
    void save(Planet planet);

    Optional<Planet> find(Number id);

    void delete(Planet planet);

    List<Planet> getAllPlanets();
}
