package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.SuggestionDTO;
import edu.netcracker.backend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SuggestionController {
    private SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("/api/v1/suggestions")
    public List<SuggestionDTO> getAllWithClassId(@RequestParam("class-id") Integer classId) {
        return suggestionService.getAllWithClassId(classId);
    }

    @GetMapping("/api/v1/suggestions/{suggestionId}")
    public SuggestionDTO getById(@PathVariable Integer suggestionId) {
        return suggestionService.getById(suggestionId);
    }

    @PostMapping("/api/v1/suggestions")
    public SuggestionDTO createSuggestion(@Valid @RequestBody SuggestionDTO suggestionDTO) {
        return suggestionService.createSuggestion(suggestionDTO);
    }

    @PutMapping("/api/v1/suggestions")
    public SuggestionDTO updateSuggestion(@Valid @RequestBody SuggestionDTO suggestionDTO) {
        return suggestionService.updateSuggestion(suggestionDTO);
    }

    @DeleteMapping("/api/v1/suggestions/{suggestionId}")
    public void deleteSuggestion(@PathVariable Integer suggestionId) {
        suggestionService.deleteSuggestion(suggestionId);
    }
}
