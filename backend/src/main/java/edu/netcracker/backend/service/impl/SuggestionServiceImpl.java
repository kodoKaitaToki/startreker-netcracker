package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PossibleServiceDAO;
import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.message.request.DiscountTicketClassDTO;
import edu.netcracker.backend.message.response.SuggestionDTO;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Suggestion;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.service.DiscountService;
import edu.netcracker.backend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import edu.netcracker.backend.message.request.DiscountSuggestionDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuggestionServiceImpl implements SuggestionService {
    private SuggestionDAO suggestionDAO;

    private PossibleServiceDAO possibleServiceDAO;

    private final DiscountService discountService;

    @Autowired
    public SuggestionServiceImpl(SuggestionDAO suggestionDAO,
                                 PossibleServiceDAO possibleServiceDAO,
                                 DiscountService discountService) {
        this.suggestionDAO = suggestionDAO;
        this.possibleServiceDAO = possibleServiceDAO;
        this.discountService = discountService;
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
                                         List<Long> newServices,
                                         List<Long> oldServices) {
        for (Long id : newServices) {
            if (!oldServices.contains(id)) {
                suggestionDAO.addPossibleService(suggestionId, id);
            }
        }

        for (Long id : oldServices) {
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

    private List<Long> toIdList(List<PossibleService> possibleServices) {
        return possibleServices.stream()
                .map(PossibleService::getPServiceId)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiscountSuggestionDTO> getSuggestionsRelatedToCarrier(Number userId) {
        List<Suggestion> suggestions = suggestionDAO.getAllSuggestionsRelatedToCarrier(userId);
        List<DiscountDTO> discountsDTO = discountService.getDiscountDTOs(suggestions.stream()
                .map(Suggestion::getDiscountId)
                .collect(Collectors.toList()));

        return createSuggestionDTOs(suggestions, discountsDTO);
    }

    @Override
    public DiscountSuggestionDTO createDiscountForSuggestion(DiscountSuggestionDTO suggestionDTO) {
        Suggestion suggestion = getSuggestion(suggestionDTO);

        DiscountDTO discountDTO = discountService.saveDiscount(suggestionDTO.getDiscountDTO());

        suggestion.setDiscountId(discountDTO.getDiscountId());
        suggestionDAO.save(suggestion);

        return DiscountSuggestionDTO.toDiscountSuggestionDTO(suggestion, discountDTO);
    }

    @Override
    public DiscountSuggestionDTO deleteDiscountForSuggestion(Number discountId, Number userId) {
        Optional<Suggestion> optionalSuggestion = suggestionDAO.getSuggestionByDiscount(userId, discountId);

        if (!optionalSuggestion.isPresent()) {
            throw new RequestException("No such discount",
                    HttpStatus.NOT_FOUND);
        }

        Suggestion suggestion = optionalSuggestion.get();
        suggestion.setDiscountId(null);
        suggestionDAO.save(suggestion);

        DiscountDTO discountDTO = discountService.deleteDiscount(discountId);

        return DiscountSuggestionDTO.toDiscountSuggestionDTO(suggestion, discountDTO);
    }

    private Suggestion getSuggestion(DiscountSuggestionDTO simpleSuggestionDTO) {
        Optional<Suggestion> optionalSuggestion = suggestionDAO.find(simpleSuggestionDTO.getSuggestionId());

        if (!optionalSuggestion.isPresent()) {
            throw new RequestException("Suggestion with id " + simpleSuggestionDTO.getSuggestionId() + " is null",
                    HttpStatus.NOT_FOUND);
        }

        Suggestion suggestion = optionalSuggestion.get();

        if (suggestion.getDiscountId() != null) {
            throw new RequestException("Discount already exist",
                    HttpStatus.CONFLICT);
        }
        return suggestion;
    }

    private List<DiscountSuggestionDTO> createSuggestionDTOs(List<Suggestion> suggestions,
                                                             List<DiscountDTO> discountDTOs) {
        List<DiscountSuggestionDTO> discountSuggestionDTOs = new ArrayList<>();
        for (Suggestion suggestion: suggestions) {
            DiscountDTO relatedDiscount = discountService.getRelatedDiscountDTO(
                    suggestion.getDiscountId(),
                    discountDTOs);
            discountSuggestionDTOs.add(DiscountSuggestionDTO.toDiscountSuggestionDTO(suggestion, relatedDiscount));
        }
        return discountSuggestionDTOs;
    }
}
