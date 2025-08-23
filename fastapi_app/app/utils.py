import hmac, hashlib, json, time, threading
from typing import Dict, Tuple

_IDEMP: Dict[str, Tuple[int, dict]] = {}
_LIMITS: Dict[str, Dict[str, float]] = {}
_LOCK = threading.Lock()

def sign_webhook(secret: str, payload: dict) -> str:
    body = json.dumps(payload, separators=(',', ':'), ensure_ascii=False).encode('utf-8')
    digest = hmac.new(secret.encode('utf-8'), body, hashlib.sha256).hexdigest()
    return f"sha256={digest}"

def idem_get(key: str): 
    if not key: return None
    with _LOCK:
        v = _IDEMP.get(key)
        return v[1] if v else None

def idem_set(key: str, value: dict):
    if not key: return
    with _LOCK:
        _IDEMP[key] = (int(time.time()), value)

def rate_bucket(ident: str, rate_per_min: int=60):
    now = time.time()
    refill = rate_per_min/60.0
    with _LOCK:
        b = _LIMITS.get(ident, {"tokens":rate_per_min, "ts": now})
        tokens = b["tokens"] + (now - b["ts"]) * refill
        if tokens > rate_per_min: tokens = rate_per_min
        if tokens < 1:
            b["tokens"] = tokens; b["ts"] = now; _LIMITS[ident] = b
            return False, int(tokens)
        b["tokens"] = tokens - 1; b["ts"] = now; _LIMITS[ident] = b
        return True, int(b["tokens"])