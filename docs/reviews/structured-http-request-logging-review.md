# Structured HTTP Request Logging Review

## Scope

Review of application-level HTTP request logging for Render Live Tail visibility.

## Findings resolved during review

1. **High-cardinality URI logging** — the implementation prefers Spring MVC's matched route template (for example `/companies/{id}`) when available, falling back to the request URI only when no route template exists.
2. **Unhandled exception status ambiguity** — if a downstream exception escapes before the response status changes, the request log records a server failure without mutating the response.
3. **Sensitive-data leakage** — dedicated tests verify that body content, query-string secrets, authorization values, cookies, and dataset sample hints do not appear in request log entries.
4. **Log injection through correlation IDs** — caller-supplied `X-Request-ID` values are constrained to a conservative character set and length; unsafe values are replaced by generated UUIDs.

## Residual considerations

- The filter emits one INFO log entry per request. If traffic becomes substantial, health-check suppression or sampling can be evaluated separately.
- Async-dispatch-specific correlation can be revisited if the application later introduces servlet async lifecycles that need explicit request-ID propagation across redispatches.

## Verdict

The change is appropriately scoped for the current portfolio API, materially improves request visibility, and avoids logging customer payload content. No blocking reviewer/adversarial finding remains.
