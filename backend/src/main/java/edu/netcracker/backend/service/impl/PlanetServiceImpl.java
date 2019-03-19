package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.service.PlanetService;
import edu.netcracker.backend.service.TicketClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {

    private static final Logger logger = LoggerFactory.getLogger(TicketClassService.class);

    @Autowired
    private PlanetDAO planetDAO;

    @Override
    public List<Planet> getAllPlanets() {
        return planetDAO.getAllPlanets();
    }
}
