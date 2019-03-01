package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.model.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class SuggestionDAOImpl extends CrudDAO<Suggestion> implements SuggestionDAO {
    private String FIND_ALL_WITH_CLASS_ID = "";

    public List<Suggestion> findAllWithClassId(Number id) {
        List<Suggestion> suggestions = new ArrayList<>();

        suggestions.addAll(getJdbcTemplate().query(
                FIND_ALL_WITH_CLASS_ID,
                new Object[]{id},
                getGenericMapper()));

        return suggestions;
    }
}
