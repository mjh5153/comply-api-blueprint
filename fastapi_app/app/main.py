import os, uuid, datetime as dt, time
from fastapi import FastAPI, Header, HTTPException, Depends, Request, Response
from fastapi.middleware.cors import CORSMiddleware
from .schemas import *
from .utils import sign_webhook, idem_get, idem_set, rate_bucket

API_PREFIX = "/v1"
DEV_API_KEY = os.getenv("API_KEY", "dev_key_123")
RATE = int(os.getenv("RATE_LIMIT_PER_MIN", "120"))

app = FastAPI(title="Comply API (FastAPI)")
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])

def require_key(authorization: str = Header(None)):
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(401, detail={"error":{"code":"unauthorized","message":"Missing token"}})
    if authorization.split(" ",1)[1] != DEV_API_KEY:
        raise HTTPException(403, detail={"error":{"code":"forbidden","message":"Invalid API key"}})

@app.get("/", include_in_schema=False)
def root(): return {"ok": True}

@app.post(API_PREFIX + "/scans", response_model=CreateScanResponse, status_code=202, dependencies=[Depends(require_key)])
def create_scan(req: CreateScanRequest, response: Response, request: Request, idempotency_key: str | None = Header(default=None, alias='Idempotency-Key')):
    ident = (request.headers.get("authorization",""))[-8:]
    ok, rem = rate_bucket(ident, RATE)
    if not ok:
        response.headers["Retry-After"] = "30"
        raise HTTPException(429, detail={"error":{"code":"rate_limited","message":"Too many requests"}})
    response.headers["X-RateLimit-Limit"] = str(RATE)
    response.headers["X-RateLimit-Remaining"] = str(rem)
    response.headers["X-RateLimit-Reset"] = "60"

    cached = idem_get(idempotency_key)
    if cached: return cached
    scan_id = "scan_" + uuid.uuid4().hex[:10]
    body = CreateScanResponse(scan_id=scan_id, status="queued", estimated_seconds=2).model_dump()
    idem_set(idempotency_key or f"default:{scan_id}", body)
    return body

@app.get(API_PREFIX + "/scans/{scan_id}", response_model=Scan)
def get_scan(scan_id: str):
    return Scan(scan_id=scan_id, type="text_policy", rule_pack="gdpr.v1", status="completed", findings=[])

@app.post(API_PREFIX + "/scans:quick", status_code=200, dependencies=[Depends(require_key)])
def quick_scan(req: CreateScanRequest, response: Response, request: Request, idempotency_key: str | None = Header(default=None, alias='Idempotency-Key')):
    ident = (request.headers.get("authorization",""))[-8:]
    ok, rem = rate_bucket(ident, RATE)
    if not ok:
        response.headers["Retry-After"] = "30"
        raise HTTPException(429, detail={"error":{"code":"rate_limited","message":"Too many requests"}})
    response.headers["X-RateLimit-Limit"] = str(RATE)
    response.headers["X-RateLimit-Remaining"] = str(rem)
    response.headers["X-RateLimit-Reset"] = "60"

    cached = idem_get(idempotency_key)
    if cached: return cached
    time.sleep(0.2)
    body = {"status":"completed", "score":0.0, "findings":[], "ttr_ms":200}
    idem_set(idempotency_key or "default", body)
    return body