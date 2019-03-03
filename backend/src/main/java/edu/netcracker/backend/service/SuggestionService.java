package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.DiscountSuggestionDTO;

import java.util.List;

public interface SuggestionService {

    List<DiscountSuggestionDTO> getSuggestionsRelatedToCarrier(Number userId);

    DiscountSuggestionDTO createDiscountForSuggestion(DiscountSuggestionDTO simpleSuggestionDTO);

    DiscountSuggestionDTO deleteDiscountForSuggestion(Number discountId, Number userId);
}
