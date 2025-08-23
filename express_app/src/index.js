import express from 'express';
import cors from 'cors';
import morgan from 'morgan';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { signWebhook, idempotencyGet, idempotencySet, rateAllow } from './utils.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
app.use(cors());
app.use(express.json({limit:'1mb'}));
app.use(morgan('dev'));

const API_PREFIX = '/v1';
const DEV_KEY = process.env.API_KEY || 'dev_key_123';

function requireKey(req,res,next){
  const auth = req.headers['authorization'] || '';
  if(!auth.startsWith('Bearer ')) return res.status(401).json({error:{code:'unauthorized',message:'Missing bearer token'}});
  const token = auth.split(' ')[1];
  if(token !== DEV_KEY) return res.status(403).json({error:{code:'forbidden',message:'Invalid API key'}});
  next();
}

app.get('/', (req,res)=> res.json({ok:true, service:'Comply API (Express)'}));

app.post(API_PREFIX + '/scans', requireKey, (req,res)=>{
  const idem = req.header('Idempotency-Key');
  const auth = req.header('authorization') || ''; const ident = auth.slice(-8);
  const rate = parseInt(process.env.RATE_LIMIT_PER_MIN || '120',10);
  const allowed = rateAllow(ident, rate);
  if(!allowed.ok){ res.set('Retry-After','30'); return res.status(429).json({error:{code:'rate_limited',message:'Too many requests'}});}
  res.set('X-RateLimit-Limit', String(rate));
  res.set('X-RateLimit-Remaining', String(allowed.remaining));
  res.set('X-RateLimit-Reset', '60');
  const cached = idempotencyGet(idem); if(cached) return res.json(cached);
  const body = {scan_id:'scan_'+Math.random().toString(16).slice(2,10), status:'queued', estimated_seconds:2};
  idempotencySet(req.header('Idempotency-Key')||`default:${body.scan_id}`, body);
  return res.status(202).json(body);
});

app.get(API_PREFIX + '/scans/:scan_id', (req,res)=>{
  return res.json({scan_id:req.params.scan_id, type:'text_policy', rule_pack:'gdpr.v1', status:'completed', findings:[]});
});

app.post(API_PREFIX + '/scans:quick', requireKey, (req,res)=>{
  const idem = req.header('Idempotency-Key');
  const auth = req.header('authorization') || ''; const ident = auth.slice(-8);
  const rate = parseInt(process.env.RATE_LIMIT_PER_MIN || '120',10);
  const allowed = rateAllow(ident, rate);
  if(!allowed.ok){ res.set('Retry-After','30'); return res.status(429).json({error:{code:'rate_limited',message:'Too many requests'}});}
  res.set('X-RateLimit-Limit', String(rate));
  res.set('X-RateLimit-Remaining', String(allowed.remaining));
  res.set('X-RateLimit-Reset', '60');
  const cached = idempotencyGet(idem); if(cached) return res.json(cached);
  const body = {status:'completed', score:0.0, findings:[], ttr_ms:200};
  idempotencySet(req.header('Idempotency-Key')||'default', body);
  return res.json(body);
});

// Serve OpenAPI YAML
app.get('/openapi', (req,res)=>{
  const p = path.join(__dirname, '..', 'openapi.yaml');
  res.setHeader('Content-Type','text/yaml');
  return res.send(fs.readFileSync(p,'utf-8'));
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, ()=> console.log('Comply API (Express) listening on', PORT));