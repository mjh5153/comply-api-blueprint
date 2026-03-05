package com.init_spring_bean_mvn.demo.controller;

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
 * Integration tests for ComplyController endpoints
 * Tests COMPLY API business operations with concurrent request handling
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ComplyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test process single compliance request
     */
    @Test
    public void testProcessSingleCompliance() throws Exception {
        String json = """
                {
                    "id": null,
                    "name": "Compliance Test Company",
                    "email": "compliance@test.com"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/comply/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Compliance Test Company"))
                .andExpect(jsonPath("$.email").value("compliance@test.com"))
                .andReturn();

        System.out.println("Compliance Process Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test batch compliance processing
     */
    @Test
    public void testProcessBatchCompliance() throws Exception {
        String json = """
                [
                    {
                        "id": null,
                        "name": "Compliance Batch 1",
                        "email": "batch1@compliance.com"
                    },
                    {
                        "id": null,
                        "name": "Compliance Batch 2",
                        "email": "batch2@compliance.com"
                    },
                    {
                        "id": null,
                        "name": "Compliance Batch 3",
                        "email": "batch3@compliance.com"
                    }
                ]
                """;

        MvcResult result = mockMvc.perform(post("/api/comply/process/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Compliance Batch 1"))
                .andExpect(jsonPath("$[1].name").value("Compliance Batch 2"))
                .andExpect(jsonPath("$[2].name").value("Compliance Batch 3"))
                .andReturn();

        System.out.println("Batch Compliance Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test concurrent external API requests
     */
    @Test
    public void testConcurrentApiRequests() throws Exception {
        String requestData = """
                {
                    "product": "apples",
                    "amount": "500",
                    "company": "InvestCross"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/comply/external-api/concurrent")
                .param("apiEndpoint", "http://localhost:8080")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andReturn();

        System.out.println("Concurrent API Response: " + result.getResponse().getContentAsString());
    }

    /**
     * Test response reconciliation
     */
    @Test
    public void testReconcileResponses() throws Exception {
        String json = """
                [
                    "{\\"status\\": \\"success\\"}",
                    "{\\"status\\": \\"success\\"}",
                    "{\\"status\\": \\"success\\"}"
                ]
                """;

        MvcResult result = mockMvc.perform(post("/api/comply/reconcile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsString("Reconciliation complete")))
                .andReturn();

        System.out.println("Reconciliation Response: " + result.getResponse().getContentAsString());
    }
}

