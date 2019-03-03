package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.PlanetsResponse;
import edu.netcracker.backend.model.Planet;

import java.util.List;

public interface PlanetsService {
    PlanetsResponse getPlanets();
}
