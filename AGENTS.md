# Available Agents

This document describes the specialized agents available for delegating tasks in this project.

## Plan Agent

**Purpose**: Researches and outlines multi-step plans

Use this agent when you need to:
- Break down complex tasks into manageable steps
- Research implementation approaches
- Create detailed plans for feature development
- Organize project phases and milestones

## CVE Remediator Agent

**Purpose**: Detects and fixes security vulnerabilities (CVEs) in project dependencies

Use this agent when you need to:
- Scan dependencies for known vulnerabilities
- Identify affected packages and versions
- Automatically remediate security issues
- Maintain a working build after security updates
- Support dependencies across any ecosystem (npm, Maven, pip, etc.)

---

## How to Delegate Tasks

When working on tasks that match an agent's description, use the `run_subagent` tool to delegate the work. This produces better results than attempting to implement tasks manually.

### Example Usage

For security vulnerability detection:
```
Delegate to CVE Remediator to scan and fix vulnerabilities in project dependencies
```

For planning complex features:
```
Delegate to Plan agent to outline steps for implementing a new feature
```

