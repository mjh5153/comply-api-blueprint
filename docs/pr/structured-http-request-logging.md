# Structured HTTP Request Logging

## What changed

- Added application-level HTTP request logging for Render Live Tail and other console-log destinations.
- Added `X-Request-ID` correlation and safe caller-supplied request-ID validation.
- Added focused tests covering success/failure metadata, sensitive-data non-logging, and log-injection resistance.
- Added observability documentation and an adversarial review note.

## Why

Render Live Tail only shows what the Spring application emits. Successful Swagger requests can therefore complete without a visible application request entry when no request logger exists. This change makes inbound request completion observable without depending on provider-generated access logs.

## Logged metadata

- request ID
- HTTP method
- URI path
- response status
- duration in milliseconds

The filter intentionally excludes request/response bodies, query strings, authorization headers, cookies, and arbitrary header values.

## Validation

GitHub Actions should run the repository's Maven CI after the pull request is opened. The new unit tests are intended to fail if sensitive sample/query/header values leak into the application request log or if unsafe request IDs are echoed into logs.
