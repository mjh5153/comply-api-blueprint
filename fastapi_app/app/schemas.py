from pydantic import BaseModel
from typing import Optional, Dict, Any, List

class CreateScanRequest(BaseModel):
    type: str
    rule_pack: str
    inputs: Dict[str, Any]

class CreateScanResponse(BaseModel):
    scan_id: str
    status: str
    estimated_seconds: Optional[int] = None

class Finding(BaseModel):
    rule_id: str
    severity: str
    message: str

class Scan(BaseModel):
    scan_id: str
    type: str
    rule_pack: str
    status: str
    findings: List[Finding] = []