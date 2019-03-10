package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.SuggestionDTO;
import edu.netcracker.backend.message.request.DiscountSuggestionDTO;
import edu.netcracker.backend.model.TicketClass;

import java.util.List;
import java.util.Map;

public interface SuggestionService {
    List<SuggestionDTO> getAllWithClassId(Number id);

    SuggestionDTO getById(Number id);

    SuggestionDTO createSuggestion(SuggestionDTO suggestionDTO);

    SuggestionDTO updateSuggestion(SuggestionDTO suggestionDTO);

    void deleteSuggestion(Number id);

    Map<Long, List<DiscountSuggestionDTO>> getSuggestionsRelatedToTicketClasses(
            Map<Long, List<TicketClass>> relatedToTripsTicketClasses);

    DiscountSuggestionDTO createDiscountForSuggestion(DiscountSuggestionDTO simpleSuggestionDTO);

    DiscountSuggestionDTO deleteDiscountForSuggestion(Number discountId, Number userId);
}
