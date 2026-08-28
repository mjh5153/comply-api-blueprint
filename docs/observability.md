# HTTP Request Observability

`comply-api-blueprint` emits one application-level log entry when each HTTP request completes. This makes request activity visible in hosting-provider application logs even when provider-generated access logs are unavailable.

Example:

```text
http_request requestId=swagger-123 method=POST path=/v1/datasets/analyze status=200 durationMs=137
```

## Correlation IDs

Clients may send a correlation ID in the `X-Request-ID` header. The API echoes a safe value in the response header and includes the same value in the request log. If the supplied value is absent or does not match the allowed identifier format, the API generates a UUID instead.

This allows a Swagger, browser, BFF, or other API client request to be correlated with the matching application log entry.

## Logged fields

The request log contains only:

- request ID
- HTTP method
- request URI path
- response status
- request duration in milliseconds

## Sensitive-data handling

The request logging filter intentionally does **not** log:

- request or response bodies
- query strings
- authorization headers
- cookies
- database credentials
- dataset sample values
- other HTTP header values

This is important for a compliance API because payloads may contain dataset metadata or other sensitive information. Feature-specific logs should follow the same principle: prefer stable identifiers, rule IDs, counts, status, and timings over customer-provided content.

## Render Live Tail

Application request logs are written through SLF4J to the normal Spring Boot console output. On Render they should therefore appear in the service's Live Tail after the deployed version containing this filter receives a request.

A useful verification flow is:

1. Open Render Live Tail for `comply-api-blueprint`.
2. Send a request from Swagger or another client.
3. Read the `X-Request-ID` response header.
4. Find the `http_request` entry with the same `requestId`.

Provider-level request logs and application-level request logs are separate features. This filter exists so request observability does not depend on provider-generated access logging.
