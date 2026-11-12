---
version: 2
name: cross-cutting-agent-configuration-components-isolate-i-c265
description: "Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Isolate Interactions Android"
metadata:
  actual:
    adr_id: c2652e4b-5571-4d7e-84b7-4a99c1292082
    status: accepted
    candidate_group_id: 06e58e7e-b441-4116-99b3-d976f277c2e2
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/agent/**
      - src/main/kotlin/com/codeassist/agenttrader/agent/**
---

# Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Isolate Interactions Android

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/agent/**`, `src/main/kotlin/com/codeassist/agenttrader/agent/**`.

These rules are ALWAYS ACTIVE for agent configuration components and modules defining agent initialization parameters that require platform resource or service access.

### Rules

- **R-AC-001** SHOULD: Agent configuration components SHOULD isolate interactions with android.content.Context behind an abstraction boundary to prevent platform dependencies from leaking into domain business logic.

### Verify

```bash
# Discover and execute the project static analysis verification script from build configuration
# Discover and run the project automated test suite
```

**Accept when:**
- All discovered static analysis checks report zero violations regarding context reference retention or unauthorized platform dependencies.
- All unit and integration tests covering agent configuration pass successfully with verified context resolution.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verified by automated code inspection, lint rules in the continuous integration pipeline, and peer code review.
</enforcement>