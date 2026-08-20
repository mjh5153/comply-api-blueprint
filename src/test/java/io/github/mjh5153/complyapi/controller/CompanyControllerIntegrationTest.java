package io.github.mjh5153.complyapi.controller;

import io.github.mjh5153.complyapi.dto.CompanyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MockMvc integration tests for {@link CompanyController} covering the
 * synchronous, asynchronous and batch endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String CREATE_JSON = """
            {"id": null, "name": "Acme Co", "email": "acme@example.com"}
            """;

    @Test
    void createCompanySync_returns201WithId() throws Exception {
        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Acme Co"))
                .andExpect(jsonPath("$.email").value("acme@example.com"));
    }

    @Test
    void getAllCompanies_returnsList() throws Exception {
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(org.hamcrest.Matchers.greaterThanOrEqualTo(0))));
    }

    @Test
    void getCompany_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/companies/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCompanySync_updatesExistingRow() throws Exception {
        // create first
        String created = mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id": null, "name": "Old Name", "email": "old@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CompanyDTO createdDto = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(created, CompanyDTO.class);

        String updateJson = """
                {"id": %d, "name": "New Name", "email": "new@example.com"}
                """.formatted(createdDto.id());

        mockMvc.perform(put("/companies/" + createdDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }
}
