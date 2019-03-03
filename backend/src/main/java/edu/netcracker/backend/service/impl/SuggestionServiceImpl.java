package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.message.response.SuggestionDTO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Suggestion;
import edu.netcracker.backend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionServiceImpl implements SuggestionService {
    private SuggestionDAO suggestionDAO;

    private PossibleServiceDAO possibleServiceDAO;

    @Autowired
    public SuggestionServiceImpl(SuggestionDAO suggestionDAO, PossibleServiceDAO possibleServiceDAO) {
        this.suggestionDAO = suggestionDAO;
        this.possibleServiceDAO = possibleServiceDAO;
    }

    @Override
    public List<SuggestionDTO> getAllWithClassId(Number id) {
        List<Suggestion> suggestions = suggestionDAO.findAllWithClassId(id);

        if (suggestions.size() == 0)
            throw new RequestException("No possible services yet", HttpStatus.NOT_FOUND);

        return suggestions.stream()
                .map(suggestion -> SuggestionDTO.from(suggestion,
                        getAttachedPServices(suggestion).stream()
                                .map(PossibleService::getPServiceId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public SuggestionDTO createSuggestion(SuggestionDTO suggestionDTO) {
        Suggestion suggestion = from(suggestionDTO);
        System.out.println("!" + suggestion.getSuggestionId());
        suggestionDAO.save(suggestion);
        System.out.println("!!" + suggestion.getSuggestionId());

        return suggestionDTO;
    }

    private List<PossibleService> getAttachedPServices(Suggestion suggestion) {
        return possibleServiceDAO.findAllPossibleServicesBySuggestionId(suggestion.getSuggestionId());
    }

    private Suggestion from(SuggestionDTO suggestionDTO) {
        Suggestion suggestion = new Suggestion();

        suggestion.setClassId(suggestionDTO.getClassId());
        suggestion.setDiscountId(suggestionDTO.getDiscountId());

        return suggestion;
    }
}
