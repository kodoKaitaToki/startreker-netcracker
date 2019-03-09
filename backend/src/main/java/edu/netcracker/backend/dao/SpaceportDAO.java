package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Spaceport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpaceportDAO {
    void save(Spaceport spaceport);

    Optional<Spaceport> find(Number id);

    List<Spaceport> findByPlanet(String planet);

    Long getIdBySpaceportName(String spaceport);

    void delete(Spaceport spaceport);

    List<Spaceport> findPerPeriod(LocalDate from, LocalDate to);
}
