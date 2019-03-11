package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.model.Suggestion;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SuggestionDAOImpl extends CrudDAOImpl<Suggestion> implements SuggestionDAO {
    private String FIND_ALL_WITH_CLASS_ID = "SELECT * FROM suggestion " +
            "WHERE class_id = ?";
    private String ADD_POSSIBLE_SERVICE = "INSERT INTO suggested_service (p_service_id, suggestion_id) " +
            "VALUES (?, ?)";
    private String DELETE_POSSIBLE_SERVICE = "DELETE FROM suggested_service WHERE p_service_id = ? AND suggestion_id = ?";

    private static final String GET_ALL_SUGGESTION_RELATED_TO_CARRIER = "SELECT "
                                                                        + "suggestion.suggestion_id, "
                                                                        + "suggestion.discount_id, "
                                                                        + "suggestion.class_id "
                                                                        + "FROM user_a "
                                                                        + "INNER JOIN trip ON trip.carrier_id = user_a.user_id "
                                                                        + "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id "
                                                                        + "INNER JOIN suggestion ON suggestion.class_id = ticket_class.class_id "
                                                                        +
                                                                        "WHERE user_a.user_id = ?";

    private static final String GET_SUGGESTION_WITH_DISCOUNT = "SELECT "
                                                               + "suggestion.suggestion_id, "
                                                               + "suggestion.discount_id, "
                                                               + "suggestion.class_id "
                                                               + "FROM user_a "
                                                               +
                                                               "INNER JOIN trip ON trip.carrier_id = user_a.user_id " +
                                                               "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id " +
                                                               "INNER JOIN suggestion ON suggestion.class_id = ticket_class.class_id " +
                                                               "WHERE user_a.user_id = ? AND suggestion.discount_id = ?";

    private static final String DELETE_DISCOUNT_CONNECTION = "UPDATE suggestion " +
            "SET discount_id = null " +
            "WHERE suggestion_id = ?";

    @Override
    public List<Suggestion> findAllWithClassId(Number id) {
        List<Suggestion> suggestions = new ArrayList<>();

        suggestions.addAll(getJdbcTemplate().query(
                FIND_ALL_WITH_CLASS_ID,
                new Object[]{id},
                getGenericMapper()));

        return suggestions;
    }

    @Override
    public void addPossibleService(Number suggestionId, Number pServiceId) {
        getJdbcTemplate().update(
                ADD_POSSIBLE_SERVICE,
                pServiceId, suggestionId);
    }

    @Override
    public void deletePossibleService(Number suggestionId, Number pServiceId) {
        getJdbcTemplate().update(
                DELETE_POSSIBLE_SERVICE,
                pServiceId, suggestionId);
    }

    @Override
    public List<Suggestion> getAllSuggestionsRelatedToCarrier(Number carrierId) {
        return new ArrayList<>(getJdbcTemplate()
                .query(GET_ALL_SUGGESTION_RELATED_TO_CARRIER,
                        new Object[]{carrierId},
                        getGenericMapper()));
    }

    @Override
    public Optional<Suggestion> getSuggestionByDiscount(Number userId, Number discountId) {
        try {
            Suggestion user = getJdbcTemplate().queryForObject(
                    GET_SUGGESTION_WITH_DISCOUNT,
                    new Object[]{userId, discountId},
                    getGenericMapper());
            return user != null ? Optional.of(user) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteDiscountsForSuggestion(List<Long> suggestionIds) {
        getJdbcTemplate().batchUpdate(DELETE_DISCOUNT_CONNECTION, suggestionIds.stream()
                .map(ticketClassId -> new Object[]{ticketClassId})
                .collect(Collectors.toList()));
    }
}
