package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.PossibleServiceDTO;
import edu.netcracker.backend.service.PossibleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PossibleServiceController {
    private PossibleServiceService possibleServiceService;

    @Autowired
    public PossibleServiceController(PossibleServiceService possibleServiceService) {
        this.possibleServiceService = possibleServiceService;
    }

    @GetMapping("/api/v1/carrier/possible-services")
    public List<PossibleServiceDTO> getAll(@RequestParam("class-id") Integer classId) {
        return possibleServiceService.getAllWithClassId(classId);
    }

    @GetMapping("/api/v1/carrier/possible-services/{possibleServiceId}")
    public PossibleServiceDTO getPossibleService(@PathVariable Integer possibleServiceId) {
        return possibleServiceService.getPossibleService(possibleServiceId);
    }

    @PostMapping("/api/v1/carrier/possible-services")
    public PossibleServiceDTO createPossibleService(@Valid @RequestBody PossibleServiceDTO possibleServiceDTO) {
        return possibleServiceService.createPossibleService(possibleServiceDTO);
    }

    @PutMapping("/api/v1/carrier/possible-services")
    public PossibleServiceDTO updatePossibleService(@Valid @RequestBody PossibleServiceDTO possibleServiceDTO) {
        return possibleServiceService.updatePossibleService(possibleServiceDTO);
    }

    @DeleteMapping("/api/v1/carrier/possible-services/{possibleServiceId}")
    public void deletePossibleService(@PathVariable Integer possibleServiceId) {
        possibleServiceService.deletePossibleService(possibleServiceId);
    }
}
