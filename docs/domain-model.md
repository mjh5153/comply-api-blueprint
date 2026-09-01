# Comply API domain model

> **Status:** Target-state domain architecture. The current blueprint persists
> `Company` records and demonstrates asynchronous processing; the remaining
> domain types in this document describe the intended dataset-to-law mapping
> model and are not yet implemented as entities or API contracts.

## Modeling principles

- A `Company` owns or uses datasets; it does not own compliance rules.
- `ComplianceRule` belongs to a versioned engine rule catalog.
- Dataset metadata and field classifications become `ComplianceFact` values.
- `RuleEvaluation` is the evidence-bearing object that connects a scan,
  matched rule, legal references, risk, triggering facts, and controls.
- Legal traceability resolves to a `LawProvision` such as GDPR Article 32,
  while `LawProvision` belongs to the broader `Law`.
- Engine findings, analyst decisions, and AI explanations remain separate
  concerns. This document models the authoritative engine path.

## 1. Core domain graph

This graph shows ownership and evaluation. The company is the business context;
the rule catalog remains independent and is applied when scan facts satisfy rule
conditions.

```mermaid
flowchart TD
    Company["Company"] -->|owns or uses| Dataset["Dataset"]
    Dataset -->|contains| Field["DataField"]
    Field -->|classified as| Category["DataCategory"]

    Dataset -->|analyzed in| Scan["Scan"]
    Scan -->|produces| Fact["ComplianceFact"]
    Scan -->|records| Evaluation["RuleEvaluation"]
    Fact -->|evaluated against| Rule["ComplianceRule"]
    Rule -->|materializes| Evaluation

    Evaluation -->|references| Provision["LawProvision"]
    Provision -->|belongs to| Law["Law"]
    Evaluation -->|recommends| Control["RecommendedControl"]
    Evaluation -->|records| Risk["RiskAssessment"]
```

## 2. Target entity relationships

Cardinalities express the target conceptual model, not the current database
schema. Persistence boundaries should be chosen during implementation rather
than inferred mechanically from this diagram.

```mermaid
erDiagram
    COMPANY ||--o{ DATASET : owns
    DATASET ||--|{ DATA_FIELD : contains
    DATA_FIELD }o--o{ DATA_CATEGORY : classified_as
    DATASET ||--o{ SCAN : analyzed_by
    SCAN ||--o{ COMPLIANCE_FACT : derives
    SCAN ||--o{ RULE_EVALUATION : records
    COMPLIANCE_RULE ||--o{ RULE_EVALUATION : evaluated_in
    COMPLIANCE_RULE }o--o{ LAW_PROVISION : references
    LAW ||--|{ LAW_PROVISION : contains
    RULE_EVALUATION }o--o{ COMPLIANCE_FACT : triggered_by
    RULE_EVALUATION }o--o{ LAW_PROVISION : cites
    RULE_EVALUATION }o--o{ RECOMMENDED_CONTROL : recommends
    RULE_EVALUATION ||--o| RISK_ASSESSMENT : assigns
```

## 3. Dataset-to-law evaluation flow

The engine first converts supplied metadata into normalized facts. Rules consume
those facts and emit evidence-backed evaluations; laws are references attached
to matched rules, not mutable properties owned by a company.

```mermaid
flowchart TD
    Input["Scan input<br/>dataset, fields, jurisdiction,<br/>role, purpose, activities"]
    Classify["Classify fields<br/>and normalize context"]
    Facts["Compliance facts"]
    Evaluate["Evaluate versioned<br/>compliance rules"]
    Result["Rule evaluations"]

    Input --> Classify
    Classify --> Facts
    Facts --> Evaluate
    Evaluate --> Result

    Result --> Finding["Applicability and risk"]
    Result --> Evidence["Triggering facts and rationale"]
    Result --> Legal["Law provisions"]
    Result --> Controls["Recommended controls"]
```

## 4. Evidence and traceability example

This example illustrates how a future investigator can answer “Why did Comply
map GDPR Article 32?” without treating an AI explanation as a new compliance
determination.

```mermaid
flowchart TD
    Dataset["Dataset<br/>CustomerTransactions"]
    Field["Field<br/>email"]
    Category["Classification<br/>CONTACT_INFORMATION"]
    Context["Context<br/>jurisdiction = EU"]
    Rule["Matched rule<br/>GDPR.PERSONAL_DATA.SECURITY.001"]
    Evaluation["Rule evaluation<br/>LIKELY_APPLICABLE · HIGH"]
    Provision["Legal reference<br/>GDPR Article 32"]
    Control["Recommended control<br/>ENCRYPT_PERSONAL_DATA"]

    Dataset --> Field
    Field --> Category
    Category --> Rule
    Context --> Rule
    Rule --> Evaluation
    Evaluation --> Provision
    Evaluation --> Control
```

## 5. Aggregate responsibilities

| Aggregate or concept | Responsibility | Ownership rule |
|---|---|---|
| `Company` | Organization using Comply | Owns datasets—not rules |
| `Dataset` | Schema and processing context under analysis | Belongs to a company |
| `DataField` / `DataCategory` | Field metadata and normalized classifications | Produces facts used by rules |
| `Scan` | Immutable analysis execution and version context | Connects input, facts, and evaluations |
| `ComplianceRule` | Versioned condition and mapping logic | Belongs to the engine rule catalog |
| `Law` / `LawProvision` | Regulation and citation-level reference | Referenced by rules and evaluations |
| `RuleEvaluation` | Evidence object for one rule result | Connects scan, rule, facts, legal references, risk, and controls |
| `RecommendedControl` | Action recommended by an evaluation | Does not itself constitute legal applicability |

## 6. Current-to-target implementation map

| Concept | Current repository status | Target role |
|---|---|---|
| `Company` | Implemented as a JPA entity and DTO | Organization boundary |
| `Dataset` | Not implemented | Unit of data being analyzed |
| `DataField` / `DataCategory` | Not implemented | Schema metadata and classification |
| `Scan` / `ComplianceFact` | Not implemented | Immutable analysis input and normalized evidence |
| `ComplianceRule` | Not implemented | Versioned rule-catalog entry |
| `Law` / `LawProvision` | Not implemented | Legal source and citation |
| `RuleEvaluation` | Not implemented | Authoritative, traceable engine result |
| `RiskAssessment` / `RecommendedControl` | Not implemented | Evaluation outputs |

## Key invariant

The core relationship is:

```text
Company -> Dataset -> Scan -> ComplianceFact
                              |
                              v
                       ComplianceRule
                              |
                              v
                       RuleEvaluation
                         /    |     \
                LawProvision Risk  Control
```

It is deliberately **not** `Company -> ComplianceRule -> Law[]`. Rules are
shared engine assets. Applicability is established only through a traceable
`RuleEvaluation` produced from a specific scan and its facts.
