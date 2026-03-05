package com.init_spring_bean_mvn.demo.controller;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for CompanyController endpoints
 * Tests both synchronous and asynchronous REST operations
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private CompanyDTO testCompany;

    @BeforeEach
    public void setUp() {
        testCompany = new CompanyDTO(null, "Test Company", "test@example.com");
    }

    /**
     * Test synchronous company creation endpoint
     */
    @Test
    public void testCreateCompanySync() throws Exception {
        String json = """
                {
                    "id": null,
                    "name": "Sync Test Company",
                    "email": "sync@test.com"
                }
                """;

        MvcResult result = mockMvc.perform(post("/companys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Sync Test Company"))
                .andExpect(jsonPath("$.email").value("sync@test.com"))
                .andReturn();

        System.out.println("Sync Create Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test asynchronous company creation endpoint
     */
    @Test
    public void testCreateCompanyAsync() throws Exception {
        String json = """
                {
                    "id": null,
                    "name": "Async Test Company",
                    "email": "async@test.com"
                }
                """;

        MvcResult result = mockMvc.perform(post("/companys/async")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Async Test Company"))
                .andExpect(jsonPath("$.email").value("async@test.com"))
                .andReturn();

        System.out.println("Async Create Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test batch async company creation
     */
    @Test
    public void testCreateCompaniesAsync() throws Exception {
        String json = """
                [
                    {
                        "id": null,
                        "name": "Batch Company 1",
                        "email": "batch1@test.com"
                    },
                    {
                        "id": null,
                        "name": "Batch Company 2",
                        "email": "batch2@test.com"
                    },
                    {
                        "id": null,
                        "name": "Batch Company 3",
                        "email": "batch3@test.com"
                    }
                ]
                """;

        MvcResult result = mockMvc.perform(post("/companys/batch/async")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Batch Company 1"))
                .andExpect(jsonPath("$[1].name").value("Batch Company 2"))
                .andExpect(jsonPath("$[2].name").value("Batch Company 3"))
                .andReturn();

        System.out.println("Batch Async Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test get all companies
     */
    @Test
    public void testGetAllCompanies() throws Exception {
        MvcResult result = mockMvc.perform(get("/companys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andReturn();

        System.out.println("Get All Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test get company by path variables
     */
    @Test
    public void testGetCompanyByPath() throws Exception {
        MvcResult result = mockMvc.perform(get("/companys/1/TestName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestName"))
                .andReturn();

        System.out.println("Get by Path Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test get company by query parameters
     */
    @Test
    public void testGetCompanyByQuery() throws Exception {
        MvcResult result = mockMvc.perform(get("/companys/query")
                .param("id", "5")
                .param("name", "QueryCompany"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("QueryCompany"))
                .andReturn();

        System.out.println("Get by Query Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test synchronous update
     */
    @Test
    public void testUpdateCompanySync() throws Exception {
        // First create a company
        String createJson = """
                {
                    "id": null,
                    "name": "Update Test",
                    "email": "update@test.com"
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/companys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract ID from response (assuming first created has ID 1 in test)
        // Then update it
        String updateJson = """
                {
                    "id": 1,
                    "name": "Updated Name",
                    "email": "updated@test.com"
                }
                """;

        MvcResult result = mockMvc.perform(put("/companys/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@test.com"))
                .andReturn();

        System.out.println("Update Sync Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test asynchronous update
     */
    @Test
    public void testUpdateCompanyAsync() throws Exception {
        String updateJson = """
                {
                    "id": 1,
                    "name": "Async Updated",
                    "email": "asyncupdate@test.com"
                }
                """;

        MvcResult result = mockMvc.perform(put("/companys/1/async")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Async Updated"))
                .andExpect(jsonPath("$.email").value("asyncupdate@test.com"))
                .andReturn();

        System.out.println("Update Async Response: " + result.getResponse().getContentAsString());
    }
}

