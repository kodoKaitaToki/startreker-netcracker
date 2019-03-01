package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Report;

import java.util.Optional;

public interface PossibleServiceDAO {
    void save(PossibleService possibleService);

    Optional<PossibleService> find(Number id);

    void delete(PossibleService possibleService);
}
