# HTTP Request Observability

`comply-api-blueprint` emits one application-level log entry when each HTTP request completes. This makes request activity visible in hosting-provider application logs even when provider-generated access logs are unavailable.

Example:

```text
http_request requestId=swagger-123 method=POST path=/v1/datasets/analyze status=200 durationMs=137
```

## Correlation IDs

Clients may send a correlation ID in the `X-Request-ID` header. The API echoes a safe value in the response header and includes the same value in the request log. If the supplied value is absent or does not match the allowed identifier format, the API generates a UUID instead.

## Logged fields

- request ID
- HTTP method
- matched route template when available, otherwise request URI path
- response status
- request duration in milliseconds

## Sensitive-data handling

The request logging filter intentionally does **not** log request or response bodies, query strings, authorization headers, cookies, database credentials, dataset sample values, or arbitrary HTTP header values.

## Render Live Tail

The request logs are written through SLF4J to Spring Boot console output. After deployment, open Render Live Tail, send a request, read the `X-Request-ID` response header, and correlate it with the matching `http_request` entry.
