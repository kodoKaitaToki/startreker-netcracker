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

    private static final String GET_ALL_SUGGESTION_RELATED_TO_CARRIER = "SELECT suggestion.* FROM user_a\n" +
            "INNER JOIN trip ON trip.carrier_id = user_a.user_id\n" +
            "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id\n" +
            "INNER JOIN suggestion ON suggestion.class_id = ticket_class.class_id\n" +
            "WHERE user_a.user_id = ?";

    private static final String GET_SUGGESTION_WITH_DISCOUNT = "SELECT suggestion.* FROM user_a " +
            "INNER JOIN trip ON trip.carrier_id = user_a.user_id " +
            "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id " +
            "INNER JOIN suggestion ON suggestion.class_id = ticket_class.class_id " +
            "WHERE user_a.user_id = ? AND suggestion.discount_id = ?";

    private static final String DELETE_DISCOUNT_CONNECTION = "UPDATE suggestion " +
            "SET discount_id = null " +
            "WHERE suggestion_id = ?";

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
