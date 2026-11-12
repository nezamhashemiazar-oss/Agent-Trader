---
version: 2
name: cross-cutting-agent-configuration-components-not-retai-eb59
description: "Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Not Retain Long"
metadata:
  actual:
    adr_id: eb59cd88-a9e7-4e47-a04d-dfe7a9ab96f2
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

# Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Not Retain Long

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/agent/**`, `src/main/kotlin/com/codeassist/agenttrader/agent/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-CFG-001** MUST_NOT: Agent configuration components MUST NOT retain long-lived references to transient or UI-bound platform context instances that exceed the lifecycle of the agent.

### Verify

```bash
# Discover the project static analysis verification script from the build configuration and execute it
# Discover and run the project automated test suite to confirm proper initialization
```

**Accept when:**
- All discovered static analysis checks report zero violations regarding context reference retention or unauthorized platform dependencies.
- All unit and integration tests covering agent configuration pass successfully with verified context resolution.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>