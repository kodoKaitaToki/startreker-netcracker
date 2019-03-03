package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.SuggestionDTO;

import java.util.List;

public interface SuggestionService {
    List<SuggestionDTO> getAllWithClassId(Number id);

    SuggestionDTO createSuggestion(SuggestionDTO suggestionDTO);
}
