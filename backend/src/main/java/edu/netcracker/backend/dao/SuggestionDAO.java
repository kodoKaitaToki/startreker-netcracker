package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Suggestion;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SuggestionDAO {
    void save(Suggestion suggestion);

    Optional<Suggestion> find(Number id);

    Optional<Suggestion> findSuggestionBelongToCarrier(Number suggestionId, Number carrierId);

    void delete(Suggestion suggestion);

    List<Suggestion> findAllWithClassId(Number id);

    void addPossibleService(Number suggestionId, Number pServiceId);

    void deletePossibleService(Number suggestionId, Number pServiceId);

    List<Suggestion> getAllSuggestionsRelatedToCarrier(Number carrierId);

    Optional<Suggestion> getSuggestionByDiscount(Number userId, Number discountId);

    void deleteDiscountsForSuggestion(List<Long> SuggestionIds);

    Map<Long, List<Suggestion>> getAllSuggestionBelongToTicketClasses(List<Number> ticketClassIds);
}
