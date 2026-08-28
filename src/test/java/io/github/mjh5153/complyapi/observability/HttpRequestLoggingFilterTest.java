package io.github.mjh5153.complyapi.observability;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.FilterChain;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HttpRequestLoggingFilterTest {

    private final HttpRequestLoggingFilter filter = new HttpRequestLoggingFilter();
    private final Logger logger = (Logger) LoggerFactory.getLogger(HttpRequestLoggingFilter.class);
    private ListAppender<ILoggingEvent> appender;

    @BeforeEach
    void captureLogs() {
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    void stopCapturingLogs() {
        logger.detachAppender(appender);
        appender.stop();
    }

    @Test
    void logsRequestMetadataAndPropagatesCallerRequestId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/datasets/analyze");
        request.addHeader(HttpRequestLoggingFilter.REQUEST_ID_HEADER, "swagger-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, successfulChain());

        assertThat(response.getHeader(HttpRequestLoggingFilter.REQUEST_ID_HEADER))
                .isEqualTo("swagger-123");
        assertThat(singleLogMessage())
                .contains("http_request")
                .contains("requestId=swagger-123")
                .contains("method=POST")
                .contains("path=/v1/datasets/analyze")
                .contains("status=200")
                .contains("durationMs=");
    }

    @Test
    void logsFailedResponseStatus() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/companies/999");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain failingChain = (servletRequest, servletResponse) ->
                ((MockHttpServletResponse) servletResponse).setStatus(500);

        filter.doFilter(request, response, failingChain);

        assertThat(singleLogMessage())
                .contains("method=GET")
                .contains("path=/companies/999")
                .contains("status=500");
    }

    @Test
    void doesNotLogBodyQueryStringOrSensitiveHeaders() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/datasets/analyze");
        request.setQueryString("token=query-secret");
        request.addHeader("Authorization", "Bearer auth-secret");
        request.addHeader("Cookie", "session=cookie-secret");
        request.setContent("{\"sample_hint\":\"person@example.com\",\"secret\":\"body-secret\"}"
                .getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, successfulChain());

        assertThat(singleLogMessage())
                .doesNotContain("query-secret")
                .doesNotContain("auth-secret")
                .doesNotContain("cookie-secret")
                .doesNotContain("person@example.com")
                .doesNotContain("body-secret");
    }

    @Test
    void rejectsUnsafeCallerRequestIdBeforeLoggingIt() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/companies");
        request.addHeader(HttpRequestLoggingFilter.REQUEST_ID_HEADER, "forged\nlog-entry");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, successfulChain());

        String responseRequestId = response.getHeader(HttpRequestLoggingFilter.REQUEST_ID_HEADER);
        assertThat(responseRequestId)
                .isNotEqualTo("forged\nlog-entry")
                .matches("[0-9a-f-]{36}");
        assertThat(singleLogMessage()).doesNotContain("forged").doesNotContain("log-entry");
    }

    private FilterChain successfulChain() {
        return (servletRequest, servletResponse) ->
                ((MockHttpServletResponse) servletResponse).setStatus(200);
    }

    private String singleLogMessage() {
        assertThat(appender.list).hasSize(1);
        return appender.list.get(0).getFormattedMessage();
    }
}
