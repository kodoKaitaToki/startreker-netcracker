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
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldBeCorrectTestCostsByCarrier() throws Exception {
        mockMvc.perform(get("/api/v1/admin/costs/51?from=2017-02-23&to=2020-02-23"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "{\"22534\":6,\"10664\":12,\"27469\":9,\"16637\":5,\"13582\":9,\"14015\":7}")));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldBeCorrectTestCosts() throws Exception {
        mockMvc.perform(get("/api/v1/admin/costs?from=2017-02-23&to=2020-02-23"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "{\"32192\":4,\"24453\":4,\"22534\":6,\"25191\":10,\"10664\":12,\"34666\":6,\"15787\":5,\"32588\":7,\"27469\":9,\"13582\":9,\"39187\":8,\"16637\":5,\"14015\":7,\"17247\":5}")));
    }
}
