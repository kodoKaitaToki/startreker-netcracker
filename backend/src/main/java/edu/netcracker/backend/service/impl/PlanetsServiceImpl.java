package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.message.response.PlanetsResponse;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.service.PlanetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanetsServiceImpl implements PlanetsService {
    private final PlanetDAO planetDAO;
    private final SpaceportDAO spaceportDAO;

    @Autowired
    public PlanetsServiceImpl(PlanetDAO planetDAO, SpaceportDAO spaceportDAO) {
        this.planetDAO = planetDAO;
        this.spaceportDAO = spaceportDAO;
    }

    public PlanetsResponse getPlanets() {
        PlanetsResponse planetsResponse = new PlanetsResponse();
        List<Planet> allPlanets = this.planetDAO.findAllPlanets();
        List<String> planetNames = new ArrayList<>();
        Map<String, List<String>> spaceportsForPlanet = new HashMap<>();

        for (Planet planet : allPlanets) {
            planetNames.add(planet.getPlanetName());
        }
        for (String planet : planetNames) {
            List<String> portNames = new ArrayList<>();
            List<Spaceport> spaceports = this.spaceportDAO.findByPlanet(planet);
            for (Spaceport port: spaceports) {
                String portName = port.getSpaceportName().substring(0, 1).toUpperCase() +
                        port.getSpaceportName().substring(1).toLowerCase();
                portNames.add(portName);
            }
            planet = planet.substring(0, 1).toUpperCase() + planet.substring(1).toLowerCase();
            spaceportsForPlanet.put(planet, portNames);
        }
        planetNames = capitalizeList(planetNames);
        planetsResponse.setPlanets(planetNames);
        planetsResponse.setSpaceports(spaceportsForPlanet);
        return planetsResponse;
    }

    private List<String> capitalizeList(List<String> toCapitalize) {
        List<String> capitalized = new ArrayList<>();
        for (String element : toCapitalize) {
            capitalized.add(element.substring(0, 1).toUpperCase() + element.substring(1).toLowerCase());
        }

        return capitalized;
    }

}
