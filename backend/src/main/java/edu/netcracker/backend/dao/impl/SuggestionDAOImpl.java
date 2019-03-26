package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.model.Suggestion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j(topic = "log")
@Repository
@PropertySource("classpath:sql/suggestiondao.properties")
public class SuggestionDAOImpl extends CrudDAOImpl<Suggestion> implements SuggestionDAO {

    @Value("${FIND_ALL_WITH_CLASS_ID}")
    private String FIND_ALL_WITH_CLASS_ID;

    @Value("${ADD_POSSIBLE_SERVICE}")
    private String ADD_POSSIBLE_SERVICE;

    @Value("${DELETE_POSSIBLE_SERVICE}")
    private String DELETE_POSSIBLE_SERVICE;

    @Value("${GET_ALL_SUGGESTION_RELATED_TO_CARRIER}")
    private static String GET_ALL_SUGGESTION_RELATED_TO_CARRIER;

    @Value("${GET_SUGGESTION_WITH_DISCOUNT}")
    private String GET_SUGGESTION_WITH_DISCOUNT;

    @Value("${DELETE_DISCOUNT_CONNECTION}")
    private String DELETE_DISCOUNT_CONNECTION;

    @Value("${GET_ALL_SUGGESTIONS_BELONG_TO_TICKET_CLASSES}")
    private String GET_ALL_SUGGESTIONS_BELONG_TO_TICKET_CLASSES;

    @Value("${GET_SUGGESTION_BELONG_TO_CARRIER}")
    private String GET_SUGGESTION_BELONG_TO_CARRIER;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public SuggestionDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Suggestion> findSuggestionBelongToCarrier(Number suggestionId, Number carrierId) {
        try {
            log.debug("Getting suggestions by suggestion (id = {}) and carrier (id = {})", suggestionId, carrierId);
            Suggestion suggestion = getJdbcTemplate().queryForObject(GET_SUGGESTION_BELONG_TO_CARRIER,
                                                                     new Object[]{carrierId, suggestionId},
                                                                     getGenericMapper());
            return Optional.of(suggestion);
        } catch (EmptyResultDataAccessException e) {
            log.error("Getting suggestions by suggestion (id = {}) and carrier (id = {}) - empty result!",
                      suggestionId,
                      carrierId);
            return Optional.empty();
        }
    }

    @Override
    public List<Suggestion> findAllWithClassId(Number id) {
        log.debug("Getting suggestions by class id {}", id);

        List<Suggestion> suggestions = new ArrayList<>();

        suggestions.addAll(getJdbcTemplate().query(FIND_ALL_WITH_CLASS_ID, new Object[]{id}, getGenericMapper()));

        return suggestions;
    }

    @Override
    public void addPossibleService(Number suggestionId, Number pServiceId) {
        log.debug("Adding possible service (id = {}) to suggestion with id {}", pServiceId, suggestionId);

        getJdbcTemplate().update(ADD_POSSIBLE_SERVICE, pServiceId, suggestionId);
    }

    @Override
    public void deletePossibleService(Number suggestionId, Number pServiceId) {
        log.debug("Removing possible service (id = {}) from suggestion with id {}", pServiceId, suggestionId);

        getJdbcTemplate().update(DELETE_POSSIBLE_SERVICE, pServiceId, suggestionId);
    }

    @Override
    public List<Suggestion> getAllSuggestionsRelatedToCarrier(Number carrierId) {
        log.debug("Getting suggestions by carrierId {}", carrierId);

        return new ArrayList<>(getJdbcTemplate().query(GET_ALL_SUGGESTION_RELATED_TO_CARRIER,
                                                       new Object[]{carrierId},
                                                       getGenericMapper()));
    }

    @Override
    public Optional<Suggestion> getSuggestionByDiscount(Number userId, Number discountId) {
        try {
            log.debug("Getting suggestion by user (id = {}) and discount (id = {})");

            Suggestion user = getJdbcTemplate().queryForObject(GET_SUGGESTION_WITH_DISCOUNT,
                                                               new Object[]{userId, discountId},
                                                               getGenericMapper());
            return user != null ? Optional.of(user) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            log.error("Getting suggestion by user (id = {}) and discount (id = {}) - empty result!");

            return Optional.empty();
        }
    }

    @Override
    public void deleteDiscountsForSuggestion(List<Long> suggestionIds) {
        log.debug("Deleting discounts by suggestions");

        getJdbcTemplate().batchUpdate(DELETE_DISCOUNT_CONNECTION,
                                      suggestionIds.stream()
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
