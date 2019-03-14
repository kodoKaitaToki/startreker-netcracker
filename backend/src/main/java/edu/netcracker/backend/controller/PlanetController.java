package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.PlanetsResponse;
import edu.netcracker.backend.service.PlanetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanetController {
    private final PlanetsService planetsService;

    @Autowired
    public PlanetController(PlanetsService planetsService) {
        this.planetsService = planetsService;
    }

    @GetMapping("api/v1/planets")
    public PlanetsResponse getPlanets() {
        return planetsService.getPlanets();
    }
}
