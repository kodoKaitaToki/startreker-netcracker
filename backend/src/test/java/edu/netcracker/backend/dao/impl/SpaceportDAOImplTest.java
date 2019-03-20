package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.model.Spaceport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class SpaceportDAOImplTest {

    List<Spaceport> spaceports;

    @Autowired
    SpaceportDAOImpl spaceportDAO = new SpaceportDAOImpl();

    @Autowired
    PlanetDAOImpl planetDAO = new PlanetDAOImpl();

    @Test
    public void findByPlanet() {
        spaceports = new ArrayList<>();
        spaceports = spaceportDAO.findByPlanet(planetDAO.findAllPlanets().get(0).getPlanetName());
        for (Spaceport port: spaceports) {
            Assert.assertEquals(port.getPlanetId(), planetDAO.findAllPlanets().get(0).getPlanetId());
        }
    }
}