---
version: 2
name: cross-cutting-integration-modules-not-introduce-altern-8a26
description: "org.json Adoption for Network Payload Serialization and Parsing: Integration Modules Not Introduce Alternative Third"
metadata:
  actual:
    adr_id: 8a261aa8-2ab1-496e-ab28-0e0ee8c67cd3
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

# org.json Adoption for Network Payload Serialization and Parsing: Integration Modules Not Introduce Alternative Third

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-JSON-001** MUST_NOT: Integration modules MUST_NOT introduce alternative third-party JSON parsing dependencies without formal architectural review and deprecation of the primary JSON library.

### Verify

```bash
# Discover and run the project dependency verification command to validate library resolution.
# Discover and run the project static analysis and test suite to ensure all network integration tests pass.
```

**Accept when:**
- All unit and integration tests covering network clients pass successfully.
- Static analysis confirms no unauthorized JSON parsing libraries are introduced in integration modules.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>