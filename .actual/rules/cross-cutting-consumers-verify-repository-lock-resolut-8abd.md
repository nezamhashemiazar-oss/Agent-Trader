---
version: 2
name: cross-cutting-consumers-verify-repository-lock-resolut-8abd
description: "org.json Adoption for Network Payload Serialization and Parsing: Consumers Verify Repository Lock Resolution Artifact"
metadata:
  actual:
    adr_id: 8abd2419-0eaa-4a41-bcbe-14edbe9bb033
    status: accepted
    candidate_group_id: be6ce5a6-c433-4c64-b82a-ff7f8882d14a
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/**
      - src/main/kotlin/com/codeassist/agenttrader/**
---

# org.json Adoption for Network Payload Serialization and Parsing: Consumers Verify Repository Lock Resolution Artifact

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-JSON-001** MUST: Consumers MUST verify the repository lock or resolution artifact to determine the exact resolved version of org.json before implementation and confirm that all invoked APIs exist in that version.

### Verify

```bash
# Discover and run project dependency verification and test suites
# (Tool discovery required per project repository manifest)
```

**Accept when:**
- All unit and integration tests covering network clients pass successfully.
- Static analysis confirms no unauthorized JSON parsing libraries are introduced in integration modules.

<enforcement>
Verification is mandatory. Claude Code MUST NOT skip or defer verification.
</enforcement>