package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.model.Suggestion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SuggestionDAOImpl extends CrudDAOImpl<Suggestion> implements SuggestionDAO {
    private String FIND_ALL_WITH_CLASS_ID = "SELECT * FROM suggestion " +
            "WHERE class_id = ?";
    private String ADD_POSSIBLE_SERVICE = "INSERT INTO suggested_service (p_service_id, suggestion_id) " +
            "VALUES (?, ?)";
    private String DELETE_POSSIBLE_SERVICE = "DELETE FROM suggested_service WHERE p_service_id = ? AND suggestion_id = ?";

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

}
