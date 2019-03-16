package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.model.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SuggestionDAOImpl extends CrudDAOImpl<Suggestion> implements SuggestionDAO {
    private String FIND_ALL_WITH_CLASS_ID = "SELECT * FROM suggestion " +
            "WHERE class_id = ?";
    private String ADD_POSSIBLE_SERVICE = "INSERT INTO suggested_service (p_service_id, suggestion_id) " +
            "VALUES (?, ?)";
    private String DELETE_POSSIBLE_SERVICE = "DELETE FROM suggested_service WHERE p_service_id = ? AND suggestion_id = ?";

    private static final String GET_ALL_SUGGESTION_RELATED_TO_CARRIER = "SELECT " +
            "suggestion.suggestion_id, " +
            "suggestion.discount_id, " +
            "suggestion.class_id " +
            "FROM user_a " +
            "INNER JOIN trip ON trip.carrier_id = user_a.user_id " +
            "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id " +
            "INNER JOIN suggestion ON suggestion.class_id = ticket_class.class_id " +
            "WHERE user_a.user_id = ?";

    private static final String GET_SUGGESTION_WITH_DISCOUNT = "SELECT " +
            "suggestion.suggestion_id, " +
            "suggestion.discount_id, " +
            "suggestion.class_id " +
            "FROM user_a " +
            "INNER JOIN trip ON trip.carrier_id = user_a.user_id " +
            "INNER JOIN ticket_class ON ticket_class.trip_id = trip.trip_id " +
            "INNER JOIN suggestion ON suggestion.class_id = ticket_class.class_id " +
            "WHERE user_a.user_id = ? AND suggestion.discount_id = ?";

    private static final String DELETE_DISCOUNT_CONNECTION = "UPDATE suggestion " +
            "SET discount_id = null " +
            "WHERE suggestion_id = ?";

    private static final String GET_ALL_SUGGESTIONS_BELONG_TO_TICKET_CLASSES = "SELECT "
                                                                               + "  suggestion_id, "
                                                                               + "  class_id, "
                                                                               + "  discount_id "
                                                                               + "FROM suggestion "
                                                                               + "WHERE class_id IN (:ticketClassIds) "
                                                                               + "ORDER BY suggestion_id DESC";

    private static final String GET_SUGGESTION_BELONG_TO_CARRIER = "SELECT "
                                                                   + "  suggestion.suggestion_id, "
                                                                   + "  suggestion.class_id, "
                                                                   + "  suggestion.discount_id "
                                                                   + "FROM user_a "
                                                                   + "INNER JOIN trip ON user_a.user_id = trip.carrier_id "
                                                                   + "INNER JOIN ticket_class ON trip.trip_id = ticket_class.trip_id "
                                                                   + "INNER JOIN suggestion ON ticket_class.class_id = suggestion.class_id "
                                                                   + "WHERE user_a.user_id = ? AND suggestion.suggestion_id = ?";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public SuggestionDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Suggestion> findSuggestionBelongToCarrier(Number suggestionId, Number carrierId) {
        try {
            Suggestion suggestion = getJdbcTemplate().queryForObject(GET_SUGGESTION_BELONG_TO_CARRIER,
                                                                     new Object[]{carrierId, suggestionId},
                                                                     getGenericMapper());
            return Optional.of(suggestion);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

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

    @Override
    public Map<Long, List<Suggestion>> getAllSuggestionBelongToTicketClasses(List<Number> ticketClassIds) {
        Map<Long, List<Suggestion>> relatedSuggestion = new HashMap<>();

        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(
                GET_ALL_SUGGESTIONS_BELONG_TO_TICKET_CLASSES,
                new MapSqlParameterSource("ticketClassIds", ticketClassIds));
        for (Map<String, Object> row : rows) {
            List<Suggestion> ticketClasses
                    = relatedSuggestion.computeIfAbsent(((Number) row.get("class_id")).longValue(),
                                                        aLong -> new ArrayList<>());

            ticketClasses.add(createSuggestion(row));
        }

        return relatedSuggestion;
    }

    private Suggestion createSuggestion(Map<String, Object> row) {
        return Suggestion.builder()
                         .suggestionId(((Number) row.get("suggestion_id")).longValue())
                         .classId(((Number) row.get("class_id")).longValue())
                         .discountId(DiscountDAO.getDiscountId(row.get("discount_id")))
                         .build();
    }
}
