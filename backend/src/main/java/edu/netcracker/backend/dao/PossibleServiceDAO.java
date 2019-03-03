package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Service;

import java.util.List;
import java.util.Optional;

public interface PossibleServiceDAO {
    void save(PossibleService possibleService);

    Optional<PossibleService> find(Number id);

    void delete(PossibleService possibleService);

    List<PossibleService> findAllWithClassId(Number id);

    List<PossibleService> findAllPossibleServicesBySuggestionId(Number suggestionId);
}
