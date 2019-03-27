package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.model.Planet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.method.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class PlanetDAOImplTest {

    @Autowired
    private PlanetDAO planetDAO = new PlanetDAOImpl();

    List<Planet> planets = new ArrayList<>();

    @Test
    public void getAllPlanets() {
        planets = planetDAO.getAllPlanets();
        assertFalse(planets.isEmpty());
    }
}