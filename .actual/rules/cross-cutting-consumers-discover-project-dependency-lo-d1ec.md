---
version: 2
name: cross-cutting-consumers-discover-project-dependency-lo-d1ec
description: "Adoption of android.content.Context in Agent Configuration: Consumers Discover Project Dependency Lock Artifact"
metadata:
  actual:
    adr_id: d1ec04f6-3138-4ae5-b2f5-9447f66207a5
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

# Adoption of android.content.Context in Agent Configuration: Consumers Discover Project Dependency Lock Artifact

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/agent/**`, `src/main/kotlin/com/codeassist/agenttrader/agent/**`.

These rules are ALWAYS ACTIVE for agent configuration components, classes requiring platform resource/service access, and modules defining agent initialization parameters that interface with platform context services.

### Rules

- **R-CTX-001** MUST: Consumers MUST discover the project dependency lock artifact and verify the exact resolved version of all platform and library dependencies prior to referencing their APIs.

### Verify

```bash
# Discover the project static analysis verification script from the build configuration and execute it
# Discover and run the project automated test suite
```

**Accept when:**
- All discovered static analysis checks report zero violations regarding context reference retention or unauthorized platform dependencies.
- All unit and integration tests covering agent configuration pass successfully with verified context resolution.

<enforcement>
Verification is mandatory. Claude Code MUST NOT skip or defer verification.
</enforcement>