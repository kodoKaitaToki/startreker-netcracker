package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.dao.SuggestionDAO;
import edu.netcracker.backend.message.request.DiscountSuggestionDTO;
import edu.netcracker.backend.model.Discount;
import edu.netcracker.backend.model.Suggestion;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.utils.DiscountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionDAO suggestionDAO;

    private final DiscountDAO discountDAO;

    @Autowired
    public SuggestionServiceImpl(DiscountDAO discountDAO, SuggestionDAO suggestionDAO) {
        this.discountDAO = discountDAO;
        this.suggestionDAO = suggestionDAO;
    }

    @Override
    public List<DiscountSuggestionDTO> getSuggestionsRelatedToCarrier(Number userId) {
        List<Suggestion> ticketClasses = suggestionDAO.getAllSuggestionsRelatedToCarrier(userId);
        List<Discount> discounts = discountDAO.findIn(ticketClasses.stream()
                .map(Suggestion::getDiscountId)
                .collect(Collectors.toList()));

        attachSuggestionsToDiscounts(ticketClasses, discounts);

        return createSimpleSuggestionDTOs(ticketClasses);
    }

    @Override
    public DiscountSuggestionDTO createDiscountForSuggestion(DiscountSuggestionDTO simpleSuggestionDTO) {
        Optional<Suggestion> optionalSuggestion = suggestionDAO.find(simpleSuggestionDTO.getSuggestionId());

        if (!optionalSuggestion.isPresent()) {
            throw new RequestException("Suggestion with id " + simpleSuggestionDTO.getSuggestionId() + " is null",
                    HttpStatus.NOT_FOUND);
        }

        if (simpleSuggestionDTO.getDiscount() == null) {
            throw new RequestException("Discount is null", HttpStatus.BAD_REQUEST);
        }

        Suggestion suggestion = optionalSuggestion.get();

        if (suggestion.getDiscountId() != null) {
            throw new RequestException("Suggestion already exist",
                    HttpStatus.CONFLICT);
        }

        Discount discount = Discount.toDiscount(simpleSuggestionDTO.getDiscount());

        discountDAO.save(discount);
        suggestion.setDiscountId(discount.getDiscountId());
        suggestion.setDiscount(discount);
        suggestionDAO.save(suggestion);

        return DiscountSuggestionDTO.toSimpleSuggestionDTO(suggestion);
    }

    @Override
    public DiscountSuggestionDTO deleteDiscountForSuggestion(Number discountId, Number userId) {
        Optional<Suggestion> optionalSuggestion = suggestionDAO.getSuggestionByDiscount(userId, discountId);

        if (!optionalSuggestion.isPresent()) {
            throw new RequestException("Discount not exist, or not allowed for this user",
                    HttpStatus.NOT_FOUND);
        }

        Suggestion suggestion = optionalSuggestion.get();
        suggestion.setDiscountId(null);
        suggestionDAO.save(suggestion);

        discountDAO.delete(discountId);

        return DiscountSuggestionDTO.toSimpleSuggestionDTO(suggestion);
    }

    private void attachSuggestionsToDiscounts(List<Suggestion> suggestions, List<Discount> discounts) {
        Map<Long, Long> suggestionIdWithOverdueDiscount = new HashMap<>();
        for (Suggestion suggestion: suggestions) {
            Discount relatedDiscount = DiscountUtils.findDiscount(suggestion.getDiscountId(), discounts);
            if (relatedDiscount!= null && DiscountUtils.isOverdueDiscount(relatedDiscount)) {
                suggestionIdWithOverdueDiscount.put(suggestion.getSuggestionId(), relatedDiscount.getDiscountId());

                suggestion.setDiscountId(null);
                continue;
            }

            suggestion.setDiscount(relatedDiscount);
        }

        deleteOverdueDiscount(suggestionIdWithOverdueDiscount);
    }

    private void deleteOverdueDiscount(Map<Long, Long> suggestionIdWithOverdueDiscount) {
        suggestionDAO.deleteDiscountsForSuggestion(new ArrayList<>(suggestionIdWithOverdueDiscount.keySet()));
        discountDAO.deleteDiscounts(new ArrayList<>(suggestionIdWithOverdueDiscount.values()));
    }

    private List<DiscountSuggestionDTO> createSimpleSuggestionDTOs(List<Suggestion> suggestions) {
        return suggestions.stream().map(DiscountSuggestionDTO::toSimpleSuggestionDTO).collect(Collectors.toList());
    }
}
