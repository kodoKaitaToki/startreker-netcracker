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

    @GetMapping("/api/v1/carrier/suggestions")
    public List<SuggestionDTO> getAllWithClassId(@RequestParam("class-id") Integer classId) {
        return suggestionService.getAllWithClassId(classId);
    }

    @PostMapping("/api/v1/carrier/suggestions")
    public SuggestionDTO createSuggestion(@Valid @RequestBody SuggestionDTO suggestionDTO) {
        return suggestionService.createSuggestion(suggestionDTO);
    }
}
