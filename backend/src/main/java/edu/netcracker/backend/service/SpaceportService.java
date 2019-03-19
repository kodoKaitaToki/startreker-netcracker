package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.SpaceportDTO;
import edu.netcracker.backend.model.Spaceport;

import java.util.List;

public interface SpaceportService {
    List<SpaceportDTO> findSpaceportsOfPlanet(Integer planetId);
}
