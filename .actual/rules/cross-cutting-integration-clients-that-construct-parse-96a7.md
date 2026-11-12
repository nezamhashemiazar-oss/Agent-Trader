---
version: 2
name: cross-cutting-integration-clients-that-construct-parse-96a7
description: "org.json Adoption for Network Payload Serialization and Parsing: Integration Clients That Construct Parse Dynamic"
metadata:
  actual:
    adr_id: 96a78f16-03e1-4143-a89c-e4b37e4fade9
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

# org.json Adoption for Network Payload Serialization and Parsing: Integration Clients That Construct Parse Dynamic

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ORGJSON-001** MUST: Integration clients that construct or parse dynamic structured JSON network payloads MUST use org.json classes, specifically JSONObject and JSONArray.
- **R-ORGJSON-002** MUST: Follow lock-version grounding before writing code that uses a versioned library by discovering the manifest, identifying the build tool, inspecting the lock/resolution artifact, fetching version-specific documentation, confirming API existence, and re-running per dependency at point of use.
- **R-ORGJSON-003** MUST: Construct request bodies using JSONObject and populate array properties using JSONArray before serializing to network streams.
- **R-ORGJSON-004** MUST: Isolate all org.json references within the integration boundary to keep domain models decoupled from raw JSON primitives.

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