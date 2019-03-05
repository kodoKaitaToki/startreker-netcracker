package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Suggestion;

import java.util.List;
import java.util.Optional;

public interface SuggestionDAO extends CrudDAO<Suggestion> {

    List<Suggestion> getAllSuggestionsRelatedToCarrier(Number carrierId);

    Optional<Suggestion> getSuggestionByDiscount(Number userId, Number discountId);

    void deleteDiscountsForSuggestion(List<Long> SuggestionIds);
}
