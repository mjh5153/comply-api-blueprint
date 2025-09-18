# Comply API Blueprint

Comply API Blueprint is an all-in-one scaffold providing OpenAPI, Postman, FastAPI, Express, Docker, GitHub Actions, webhook signing, idempotency, and rate limits for building compliance APIs.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Bootstrap and Setup
- Install runtime dependencies:
  - Node.js 20+ for Express app: Use existing system Node.js (/usr/local/bin/node)
  - Python 3.11+ for FastAPI app: Use existing system Python (/usr/bin/python3)
  - Docker and Docker Compose for containerized deployment

### Build and Run Applications

#### Express App (Node.js)
- Change to Express directory: `cd express_app`
- Install dependencies: `npm install` -- takes ~4 seconds, NEVER CANCEL
- Run development server: `npm run dev` or `npm start`
- Default port: 3000 (configurable via PORT environment variable)
- Test endpoint: `curl http://localhost:3000/` should return `{"ok":true,"service":"Comply API (Express)"}`

#### FastAPI App (Python)  
- Change to FastAPI directory: `cd fastapi_app`
- Install dependencies: `pip install -r requirements.txt` -- takes ~10 seconds, NEVER CANCEL
- Run development server: `uvicorn app.main:app --host 0.0.0.0 --port 8000`
- Default port: 8000 (configurable via PORT environment variable)
- Test endpoint: `curl http://localhost:8000/` should return `{"ok":true}`

#### Docker Deployment
- Build and run with Docker Compose: `docker compose up --build` 
- **WARNING**: Docker builds may fail in environments with SSL certificate issues
- If Docker fails, run applications directly using the Node.js and Python commands above
- FastAPI container port: 8000
- Express container port: 3000

## Validation and Testing

### Manual API Testing
Always test API functionality after making changes:

#### Express API Testing
```bash
# Test health endpoint
curl http://localhost:3000/

# Test quick scan endpoint (requires auth)
curl -H "Authorization: Bearer dev_key_123" \
     -H "Content-Type: application/json" \
     -d '{"text": "test data"}' \
     http://localhost:3000/v1/scans:quick
```

Expected response: `{"status":"completed","score":0,"findings":[],"ttr_ms":200}`

#### FastAPI Testing
```bash
# Test health endpoint  
curl http://localhost:8000/

# Test quick scan endpoint (requires correct schema)
curl -H "Authorization: Bearer dev_key_123" \
     -H "Content-Type: application/json" \
     -d '{"type": "text_policy", "rule_pack": "gdpr.v1", "inputs": {"text": "test data"}}' \
     http://localhost:8000/v1/scans:quick
```

Expected response: `{"status":"completed","score":0.0,"findings":[],"ttr_ms":200}`

### Complete Validation Scenarios
Run these scenarios to ensure full functionality:

#### Authentication Testing
```bash
# Test missing authorization (should return 401)
curl -H "Content-Type: application/json" \
     -d '{"type": "text_policy", "rule_pack": "gdpr.v1", "inputs": {"text": "test"}}' \
     http://localhost:8000/v1/scans:quick

# Test invalid API key (should return 403)  
curl -H "Authorization: Bearer invalid_key" \
     -H "Content-Type: application/json" \
     -d '{"type": "text_policy", "rule_pack": "gdpr.v1", "inputs": {"text": "test"}}' \
     http://localhost:8000/v1/scans:quick
```

#### Feature Testing
```bash
# Test idempotency (same key should return cached result)
curl -H "Authorization: Bearer dev_key_123" \
     -H "Content-Type: application/json" \
     -H "Idempotency-Key: test-123" \
     -d '{"type": "text_policy", "rule_pack": "gdpr.v1", "inputs": {"text": "test"}}' \
     http://localhost:8000/v1/scans:quick

# Test rate limiting headers are present
curl -v -H "Authorization: Bearer dev_key_123" \
     -H "Content-Type: application/json" \
     -d '{"type": "text_policy", "rule_pack": "gdpr.v1", "inputs": {"text": "test"}}' \
     http://localhost:8000/v1/scans:quick 2>&1 | grep -i ratelimit

# Test scan retrieval endpoint
curl http://localhost:8000/v1/scans/any-scan-id
```

### Environment Configuration
Both applications use these environment variables:
- `API_KEY` (default: "dev_key_123"): Bearer token for API authentication
- `WEBHOOK_SECRET` (default: "dev_webhook_secret"): Secret for webhook signing
- `RATE_LIMIT_PER_MIN` (default: "120"): Rate limit per minute per user

### CI/CD Pipeline
The repository includes GitHub Actions workflows:
- `.github/workflows/ci.yml`: Builds both Docker images and tests basic functionality
- `.github/workflows/release-docker.yml`: Publishes Docker images on version tags
- CI builds may take 5-10 minutes depending on network conditions. NEVER CANCEL build processes.

## API Schema and Structure

### FastAPI Request Format
FastAPI endpoints require specific schema (see `fastapi_app/app/schemas.py`):
```json
{
  "type": "text_policy",
  "rule_pack": "gdpr.v1", 
  "inputs": {"text": "content to scan"}
}
```

### Express Request Format  
Express endpoints accept simpler payloads:
```json
{"text": "content to scan"}
```

### Key API Endpoints
- `GET /`: Health check endpoint
- `POST /v1/scans`: Create asynchronous scan (202 response)
- `POST /v1/scans:quick`: Create synchronous scan (200 response)  
- `GET /v1/scans/{scan_id}`: Get scan results
- `GET /openapi` (Express only): Returns OpenAPI specification

## Common Tasks and File Locations

### Key Project Files
- `docker-compose.yml`: Multi-service container orchestration
- `bootstrap.sh`: Project setup and scaffolding script
- `openapi/openapi.yaml`: OpenAPI 3.0 specification
- `postman/ComplyAPI.postman_collection.json`: Postman test collection
- `.env.example`: Environment variable template

### Express App Structure
```
express_app/
├── Dockerfile
├── package.json          # Dependencies and scripts
├── openapi.yaml         # Express-specific OpenAPI spec
└── src/
    ├── index.js         # Main application server
    └── utils.js         # Utilities (webhook, idempotency, rate limiting)
```

### FastAPI App Structure  
```
fastapi_app/
├── Dockerfile
├── requirements.txt     # Python dependencies
└── app/
    ├── __init__.py
    ├── main.py         # Main FastAPI application
    ├── schemas.py      # Pydantic data models
    └── utils.py        # Utilities (webhook, idempotency, rate limiting)
```

## Troubleshooting

### Docker Build Issues
- SSL certificate errors are common in restricted network environments
- Workaround: Run applications directly with Node.js and Python instead of Docker
- If Docker is required, try building with `--no-cache` flag

### Application Not Starting
- Check port availability: `lsof -i :3000` or `lsof -i :8000`  
- Verify environment variables are set correctly
- Check logs for import/dependency errors

### API Authentication Failures
- Ensure Authorization header format: `Bearer dev_key_123`
- Default API key is "dev_key_123" (configurable via API_KEY environment variable)
- Rate limiting may block requests (120/minute by default)

### Schema Validation Errors (FastAPI)
- FastAPI requires strict schema adherence (type, rule_pack, inputs fields)
- Express is more flexible with request payloads
- Check `fastapi_app/app/schemas.py` for exact requirements

## Development Guidelines

### Making Changes
- Always test both Express and FastAPI applications after modifications
- Run manual API tests using curl commands provided above
- Verify rate limiting and idempotency features are working
- Test with and without authentication headers
- No linting tools are configured - follow existing code style
- No automated test framework - rely on manual validation

### Feature Development
- Both apps implement the same API surface with slight differences in request/response formats
- Webhook signing, rate limiting, and idempotency are implemented in both apps
- Rate limiting uses token bucket algorithm (see utils.js/utils.py)
- Idempotency keys prevent duplicate request processing