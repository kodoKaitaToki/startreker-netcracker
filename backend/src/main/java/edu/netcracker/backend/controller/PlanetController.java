package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.PlanetsResponse;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanetController {

    private final PlanetService planetService;

    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @GetMapping("api/v1/planets")
    public List<Planet> getAllPlanets() {
        return planetService.getAllPlanets();
    }

    @GetMapping("api/v1/planets-spaceports")
    public PlanetsResponse getPlanets() {
        return planetService.getPlanets();
    }
}
