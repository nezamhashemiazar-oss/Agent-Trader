---
version: 2
name: cross-cutting-agent-configuration-components-that-requ-e90c
description: "Adoption of android.content.Context in Agent Configuration: Agent Configuration Components That Require Platform"
metadata:
  actual:
    adr_id: e90c2f43-bae3-4407-a499-c92d8e90d61d
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

# Adoption of android.content.Context in Agent Configuration: Agent Configuration Components That Require Platform

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/agent/**`, `src/main/kotlin/com/codeassist/agenttrader/agent/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ADR-001** MUST: Agent configuration components that require platform resource access MUST receive android.content.Context solely through explicit dependency injection at initialization rather than static global access.

### Verify

```bash
# Discover and run the project static analysis verification script from the build configuration
# Discover and run the project automated test suite
```

**Accept when:**
- All discovered static analysis checks report zero violations regarding context reference retention or unauthorized platform dependencies.
- All unit and integration tests covering agent configuration pass successfully with verified context resolution.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>