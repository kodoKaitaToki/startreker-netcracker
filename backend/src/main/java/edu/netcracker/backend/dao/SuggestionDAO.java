package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Suggestion;
import edu.netcracker.backend.model.TicketClass;

import java.util.List;
import java.util.Optional;

public interface SuggestionDAO {
    void save(Suggestion suggestion);

    Optional<Suggestion> find(Number id);

    void delete(Suggestion suggestion);

    List<Suggestion> findAllWithClassId(Number id);

    void addPossibleService(Number suggestionId, Number pServiceId);

    void deletePossibleService(Number suggestionId, Number pServiceId);
}
