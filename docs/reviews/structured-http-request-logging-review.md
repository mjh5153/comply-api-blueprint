# Structured HTTP Request Logging Review

## Scope

Review of the application-level HTTP request logging added for Render Live Tail visibility.

## Reviewer / adversarial findings

### Accepted design choices

- Uses a servlet filter so every request is observed without duplicating controller logging.
- Emits only request ID, method, URI path, response status, and duration.
- Does not read or log request/response bodies.
- Does not log query strings, authorization headers, cookies, or arbitrary header values.
- Propagates `X-Request-ID` for correlation between Swagger/BFF clients and application logs.
- Restricts caller-supplied request IDs to a conservative character set and length to prevent newline/log-injection content from being echoed into logs.
- Requires no new dependency because the existing Spring Boot web/logging stack already provides the needed filter and SLF4J support.

### Risks considered

- **Sensitive-data leakage:** mitigated by never logging body, query string, cookies, authorization, or arbitrary headers.
- **Log injection:** mitigated by validating caller-supplied request IDs before echoing/logging them.
- **High-cardinality path values:** current implementation logs the servlet request URI. This is acceptable for the portfolio API's current route surface, but a future observability hardening change should prefer Spring's matched route template (for example `/companies/{id}`) when available.
- **Unhandled exception status:** normal Spring MVC handled failures expose their final HTTP status to the filter. A future hardening change may explicitly mark an unhandled exception when a downstream exception escapes before the response status is updated.
- **Duplicate platform access logs:** application logs and Render-generated request logs are separate. This filter is intentional because application-level visibility should not depend on provider access-log features.

## Verdict

The change is appropriately scoped for a portfolio deployment and materially improves request visibility without logging customer payload content. No blocking finding was identified for the current API surface.
