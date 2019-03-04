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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
            throw new RequestException("No suggestions yet", HttpStatus.NOT_FOUND);

        return suggestions.stream()
                .map(suggestion -> SuggestionDTO.from(suggestion,
                        getAttachedPServices(suggestion).stream()
                                .map(PossibleService::getPServiceId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public SuggestionDTO getById(Number id) {
        Optional<Suggestion> optSuggestion = suggestionDAO.find(id);

        if (!optSuggestion.isPresent())
            throw new RequestException("Suggestion with id " + id + " not found", HttpStatus.NOT_FOUND);

        Suggestion suggestion = optSuggestion.get();

        return SuggestionDTO.from(suggestion, toIdList(getAttachedPServices(suggestion)));
    }

    @Override
    public SuggestionDTO createSuggestion(SuggestionDTO suggestionDTO) {
        if (suggestionDTO.getPServiceIds().size() == 0)
            throw new RequestException("No services attached", HttpStatus.BAD_REQUEST);

        Suggestion suggestion = from(suggestionDTO);
        suggestionDAO.save(suggestion);

        suggestionDTO.getPServiceIds().forEach(item -> {
            Optional<PossibleService> optPossibleService = possibleServiceDAO.find(item);

            if (!optPossibleService.isPresent())
                throw new RequestException("Possible service with id " + item + " not found", HttpStatus.NOT_FOUND);

            suggestionDAO.addPossibleService(suggestion.getSuggestionId(), item);
        });

        return SuggestionDTO.from(suggestion, suggestionDTO.getPServiceIds());
    }

    @Override
    public SuggestionDTO updateSuggestion(SuggestionDTO suggestionDTO) {
        Optional<Suggestion> optSuggestion = suggestionDAO.find(suggestionDTO.getId());

        if (!optSuggestion.isPresent())
            throw new RequestException("Suggestion with id " + suggestionDTO.getId() + " not found",
                    HttpStatus.NOT_FOUND);

        Suggestion suggestion = from(suggestionDTO);
        suggestionDAO.save(suggestion);

        updateAttachedPServices(suggestion.getSuggestionId(),
                suggestionDTO.getPServiceIds(),
                toIdList(getAttachedPServices(optSuggestion.get())));

        return SuggestionDTO.from(suggestion, toIdList(getAttachedPServices(suggestion)));
    }

    @Override
    public void deleteSuggestion(Number id) {
        Optional<Suggestion> optSuggestion = suggestionDAO.find(id);

        if (!optSuggestion.isPresent())
            throw new RequestException("Suggestion with id " + id + " not found",
                    HttpStatus.NOT_FOUND);

        Suggestion suggestion = optSuggestion.get();
        toIdList(getAttachedPServices(suggestion)).forEach(i -> suggestionDAO.deletePossibleService(id, i));

        suggestionDAO.delete(suggestion);
    }

    private List<PossibleService> getAttachedPServices(Suggestion suggestion) {
        return possibleServiceDAO.findAllPossibleServicesBySuggestionId(suggestion.getSuggestionId());
    }

    private void updateAttachedPServices(Number suggestionId,
                                         List<Integer> newServices,
                                         List<Integer> oldServices) {
        for (Integer id : newServices) {
            System.out.print(id + " !");
            if (!oldServices.contains(id)) {
                suggestionDAO.addPossibleService(suggestionId, id);
            }
        }

        System.out.println("   ");

        for (Integer id : oldServices) {
            System.out.print(id + " !!");

            if (!newServices.contains(id)) {
                suggestionDAO.deletePossibleService(suggestionId, id);
            }
        }
    }

    private Suggestion from(SuggestionDTO suggestionDTO) {
        Suggestion suggestion = new Suggestion();

        suggestion.setSuggestionId(suggestionDTO.getId());
        suggestion.setClassId(suggestionDTO.getClassId());
        suggestion.setDiscountId(suggestionDTO.getDiscountId());

        return suggestion;
    }

    private List<Integer> toIdList(List<PossibleService> possibleServices) {
        return possibleServices.stream()
                .map(PossibleService::getPServiceId)
                .collect(Collectors.toList());
    }
}
