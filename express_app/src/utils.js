import crypto from 'crypto';
const IDEMP = new Map();
const LIMITS = new Map();

export function signWebhook(secret, payload){
  const body = Buffer.from(JSON.stringify(payload));
  const h = crypto.createHmac('sha256', secret).update(body).digest('hex');
  return `sha256=${h}`;
}

export function idempotencyGet(key){ if(!key) return null; return IDEMP.get(key)||null; }
export function idempotencySet(key,v){ if(!key) return; IDEMP.set(key,v); }

export function rateAllow(identifier, ratePerMin=60){
  const now = Date.now()/1000;
  let b = LIMITS.get(identifier) || {tokens: ratePerMin, ts: now};
  const refill = ratePerMin/60;
  let tokens = b.tokens + (now - b.ts) * refill;
  if (tokens > ratePerMin) tokens = ratePerMin;
  if (tokens < 1){ b.tokens = tokens; b.ts = now; LIMITS.set(identifier,b); return {ok:false, remaining:Math.floor(tokens)}; }
  b.tokens = tokens - 1; b.ts = now; LIMITS.set(identifier,b);
  return {ok:true, remaining:Math.floor(b.tokens)};
}