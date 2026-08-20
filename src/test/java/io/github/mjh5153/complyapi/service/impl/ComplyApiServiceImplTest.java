package io.github.mjh5153.complyapi.service.impl;

import io.github.mjh5153.complyapi.dto.CompanyDTO;
import io.github.mjh5153.complyapi.service.AsyncHttpService;
import io.github.mjh5153.complyapi.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Verifies the orchestration layer wires {@code CompanyService} and
 * {@code AsyncHttpService} together correctly.
 */
@ExtendWith(MockitoExtension.class)
class ComplyApiServiceImplTest {

    @Mock CompanyService companyService;
    @Mock AsyncHttpService asyncHttpService;
    @InjectMocks ComplyApiServiceImpl service;

    @Test
    void processComplianceRequest_delegatesToCompanyServiceAsync() throws Exception {
        CompanyDTO input = new CompanyDTO(null, "X", "x@x.com");
        CompanyDTO saved = new CompanyDTO(1L, "X", "x@x.com");
        given(companyService.createCompanyAsync(input))
                .willReturn(CompletableFuture.completedFuture(saved));

        CompanyDTO result = service.processComplianceRequest(input).get();

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void sendConcurrentApiRequests_returnsAllResponses() throws Exception {
        Map<String, String> data = Map.of("k", "v");
        given(asyncHttpService.sendConcurrentPostRequests("http://x", data))
                .willReturn(CompletableFuture.completedFuture(List.of("r1", "r2")));

        List<String> result = service.sendConcurrentApiRequests("http://x", data).get();

        assertThat(result).containsExactly("r1", "r2");
    }

    @Test
    void reconcileApiResponses_countsNonEmptyEntries() throws Exception {
        // Arrays.asList permits nulls, unlike List.of, so we can exercise the
        // "null or empty" branch in ComplyApiServiceImpl.reconcileApiResponses.
        String result = service.reconcileApiResponses(
                Arrays.asList("ok", "", "ok", null)).get();

        assertThat(result).contains("2/4");
    }
}
