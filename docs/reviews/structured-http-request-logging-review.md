# Structured HTTP Request Logging Review

## Scope

Review of the application-level HTTP request logging added for Render Live Tail visibility.

## Reviewer / adversarial findings

### Accepted design choices

- Uses a servlet filter so every request is observed without duplicating controller logging.
- Emits only request ID, method, normalized path, response status, and duration.
- Does not read or log request/response bodies.
- Does not log query strings, authorization headers, cookies, or arbitrary header values.
- Propagates `X-Request-ID` for correlation between Swagger/BFF clients and application logs.
- Restricts caller-supplied request IDs to a conservative character set and length to prevent newline/log-injection content from being echoed into logs.
- Requires no new dependency because the existing Spring Boot web/logging stack already provides the needed filter and SLF4J support.

### Findings resolved during review

1. **High-cardinality URI logging** — the first pass logged the raw servlet URI. The reviewed implementation now prefers Spring MVC's matched route template (for example `/companies/{id}`) when available, falling back to the request URI only when no route template exists.
2. **Unhandled exception status ambiguity** — the first pass could log a successful status if a downstream exception escaped before the response status changed. The reviewed implementation records such an unhandled escape as a server failure in the request log without mutating the response.
3. **Sensitive-data leakage** — dedicated tests verify that body content, query-string secrets, authorization values, cookies, and dataset sample hints do not appear in the log entry.
4. **Log injection through correlation IDs** — dedicated tests verify that unsafe caller-supplied request IDs are rejected and replaced with a generated UUID.

### Residual considerations

- Provider-level access logs and application-level logs remain separate features; this filter intentionally provides application visibility independent of the host's access-log plan.
- The filter logs one INFO entry per request. If traffic becomes substantial, sampling or route-specific suppression for noisy endpoints such as health checks can be evaluated separately.
- Async-dispatch-specific correlation can be revisited if the application later adds servlet async request lifecycles that require request-ID propagation across redispatches.

## Verdict

The final change is appropriately scoped for a portfolio deployment and materially improves request visibility without logging customer payload content. The adversarial findings identified in the first pass were resolved before opening the pull request. No blocking finding remains for the current API surface.
