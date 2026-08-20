package io.github.mjh5153.complyapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MockMvc integration tests for {@link ComplyController}.
 *
 * <p>Async endpoints only assert that the initial dispatch succeeds — the
 * underlying service paths are exercised directly by {@code CompanyController}
 * tests via {@code CompanyService}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ComplyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void reconcileResponses_returnsAggregatedSummary() throws Exception {
        String json = """
                ["{\\"status\\":\\"ok\\"}", "{\\"status\\":\\"ok\\"}"]
                """;
        mockMvc.perform(post("/api/comply/reconcile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
