---
version: 2
name: cross-cutting-agent-configuration-components-accept-sp-9b17
description: "Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Accept Specialized Scoped"
metadata:
  actual:
    adr_id: 9b1782f9-031f-42e0-b467-6db7ecd3e18d
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

# Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Accept Specialized Scoped

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/agent/**`, `src/main/kotlin/com/codeassist/agenttrader/agent/**`.

These rules are ALWAYS ACTIVE for agent configuration components, modules defining agent initialization parameters, and classes that require platform resource or service access.

### Rules

- **R-AGD-001** MAY: Agent configuration components MAY accept a specialized or scoped context instance when only a subset of platform capabilities is required.

### Verify

```bash
# Discover and execute the project static analysis verification script from build configuration
# Discover and run the project automated test suite
```

**Accept when:**
- All discovered static analysis checks report zero violations regarding context reference retention or unauthorized platform dependencies.
- All unit and integration tests covering agent configuration pass successfully with verified context resolution.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>