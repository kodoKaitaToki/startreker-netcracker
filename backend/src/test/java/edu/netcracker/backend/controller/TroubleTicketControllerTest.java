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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class TroubleTicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldBeCorrectTestStatistics() throws Exception {
        mockMvc.perform(post("/api/trouble/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "{\"amount\":{\"total\":6.0,\"total_reopened\":1.0,\"total_opened\":1.0,\"total_rated\":1.0,\"total_answered\":1.0,\"avg_rate\":1.0,\"total_in_progress\":1.0,\"total_resolved\":2.0}}")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldBeCorrectTestStatisticsByApprover() throws Exception {
        mockMvc.perform(post("/api/trouble/statistics/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "{\"amount\":{\"total\":0.0,\"total_reopened\":0.0,\"total_opened\":0.0,\"total_rated\":0.0,\"total_answered\":0.0,\"avg_rate\":0.0,\"total_in_progress\":0.0,\"total_resolved\":0.0}}")));
    }
}
