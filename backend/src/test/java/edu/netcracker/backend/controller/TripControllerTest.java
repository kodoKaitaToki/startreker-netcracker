package edu.netcracker.backend.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldBeCorrectTestDistribution() throws Exception {
        mockMvc.perform(get("/api/v1/trip/distribution"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "[{\"departure_id\":17,\"arrival_id\":9,\"departure_planet_id\":4,\"arrival_planet_id\":1,\"departure_spaceport_name\":\"libero\",\"arrival_spaceport_name\":\"neque\",\"departure_planet_name\":\"MARS\",\"arrival_planet_name\":\"EARTH\",\"occurrence_count\":1,\"percentage\":14.285714285714285},")));
    }
}
