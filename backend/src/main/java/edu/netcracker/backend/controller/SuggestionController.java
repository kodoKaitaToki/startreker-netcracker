package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.SuggestionDTO;
import edu.netcracker.backend.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/suggestions")
public class SuggestionController {
    private SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping
    public List<SuggestionDTO> getAllWithClassId(@RequestParam("class-id") Integer classId) {
        return suggestionService.getAllWithClassId(classId);
    }

    @GetMapping("/{suggestionId}")
    public SuggestionDTO getById(@PathVariable Integer suggestionId) {
        return suggestionService.getById(suggestionId);
    }

    @PostMapping
    @PreAuthorize("hasRole('CARRIER')")
    public SuggestionDTO createSuggestion(@Valid @RequestBody SuggestionDTO suggestionDTO) {
        return suggestionService.createSuggestion(suggestionDTO);
    }

    @PutMapping
    @PreAuthorize("hasRole('CARRIER')")
    public SuggestionDTO updateSuggestion(@Valid @RequestBody SuggestionDTO suggestionDTO) {
        return suggestionService.updateSuggestion(suggestionDTO);
    }

    @DeleteMapping("/{suggestionId}")
    @PreAuthorize("hasRole('CARRIER')")
    public void deleteSuggestion(@PathVariable Integer suggestionId) {
        suggestionService.deleteSuggestion(suggestionId);
    }
}
