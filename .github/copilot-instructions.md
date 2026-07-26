# FC26 GitHub Copilot Instructions

Use these repository instructions when working on FC26 with GitHub Copilot.

## Spec-Driven Development

- Use GitHub Spec Kit skills for new feature work and material behavior changes.
- Start substantial work with `/speckit-specify`, then use `/speckit-clarify`, `/speckit-plan`, `/speckit-checklist`, `/speckit-tasks`, `/speckit-analyze`, `/speckit-implement`, and `/speckit-converge` as needed.
- Read `.specify/memory/constitution.md` before planning or implementing FC26 behavior.
- Read `docs/specs/fc26-early-warning-system-spec.md` before changing product behavior.
- Read `docs/specs/fc26-analysis-code-review-spec.md` before reviewing or changing analysis, scoring, alerting, dashboard, social network analysis, calibration, security, or deployment behavior.
- State which spec requirement each code change satisfies.
- Update the relevant spec when behavior, data contracts, access model, alert routing, reporting audience, or governance assumptions change.

## Safety and Governance

- Use only public, licensed, user-provided, or otherwise authorized social-platform data.
- Do not implement unauthorized scraping, private-group access, platform access evasion, voter persuasion, negative campaigning, or targeted political persuasion.
- Treat nonprofit status, campaign coordination, funding restrictions, invite-only access, and public dashboard constraints as counsel-reviewed requirements.
- Require human review before labeling people, accounts, groups, organizations, or networks as malicious, coordinated, synthetic, or foreign-linked.
- Frame outputs as evidence-based civic integrity analysis, public-interest correction, platform reporting, or reviewer triage.

## Agentic Workflow Rules

- Identify which workflow pattern is being used for agent changes: planning, tool use, ReAct, reflection, or multi-agent collaboration.
- Define typed inputs and outputs for every agent, tool call, handoff, score, report, and dashboard contract.
- Add explicit stop conditions, failure states, retries, timeouts, audit logs, and test doubles for agent and tool workflows.
- Do not treat model output as verified evidence without provenance, confidence, uncertainty, and human-review gates.

## Validation

- Prefer focused tests for touched Python core logic and dashboard builds for touched frontend code.
- Add mocked agent workflow tests for tool permissions, schema handoffs, stop conditions, reflection/review gates, and prompt-injection fixtures when touching bot orchestration.
- Do not run live Gemini, Watson, BigQuery, Cloud Run, social-platform APIs, or other cost-bearing actions without explicit approval.
- Preserve audit trails for model calls, scoring decisions, graph evidence, and reviewer actions whenever possible.

## Code Review

- Lead reviews with findings or architectural observations, ordered by severity.
- Classify findings as Confirmed Issue, Architectural Concern, Best Practice Gap, or Question when using the FC26 review rubric.
- Review security, platform compliance, privacy, GCP/IAM, calibration, testing, observability, social network analysis, alert routing, dashboard contracts, and public/private reporting boundaries.
