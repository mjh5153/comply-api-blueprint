# GitHub Copilot Instructions

# Full-Stack Wizard Guidelines

## Tech Stack
- **Frontend:** Angular (latest stable), focusing on Standalone Components and Signals. Use Angular Material for UI.
- **Backend:** Java with Spring Boot. Focus on reactive patterns (WebFlux) where performance is critical.
- **Database:** PostgreSQL/Hibernate with optimized indexing and connection pooling.

## Performance & Optimization
- **Web/Mobile:** Apply lazy loading, tree-shaking, and image optimization for mobile. Prioritize Core Web Vitals (LCP, FID, CLS).
- **Bundle Size:** Minimize third-party dependencies in Angular; favor native browser APIs.
- **Backend Latency:** Implement caching (Redis/Ehcache) and asynchronous processing for long-running tasks.

## Compliance & Security
- **Data Privacy:** Ensure GDPR/CCPA compliance in data handling (e.g., encryption at rest, PII masking).
- **OWASP Standards:** Validate all inputs and prevent XSS, CSRF, and SQL Injection.
- **Audit Trails:** Every DB mutation must include an audit trail (created_by, modified_at).


This file contains coding standards and guidelines for this project.

---

## applyTo: "**"

# Project general coding standards

## Naming Conventions
- Use PascalCase for component names, interfaces, and type aliases
- Use camelCase for variables, functions, and methods
- Prefix private class members with underscore (_)
- Use ALL_CAPS for constants

## Error Handling
- Use try/catch blocks for async operations
- Implement proper error boundaries in React components
- Always log errors with contextual information

---

## applyTo: "docs/**/*.md"

# Project documentation writing guidelines

## General Guidelines
- Write clear and concise documentation.
- Use consistent terminology and style.
- Include code examples where applicable.

## Grammar
* Use present tense verbs (is, open) instead of past tense (was, opened).
* Write factual statements and direct commands. Avoid hypotheticals like "could" or "would".
* Use active voice where the subject performs the action.
* Write in second person (you) to speak directly to readers.

## Markdown Guidelines
- Use headings to organize content.
- Use bullet points for lists.
- Include links to related resources.
- Use code blocks for code snippets.

