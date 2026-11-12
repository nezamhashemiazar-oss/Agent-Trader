---
version: 2
name: cross-cutting-components-handling-network-payloads-enc-ae4d
description: "org.json Adoption for Network Payload Serialization and Parsing: Components Handling Network Payloads Encapsulate Jsonobject"
metadata:
  actual:
    adr_id: ae4dad4d-f0a3-408b-8408-e638d0ff2847
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

# org.json Adoption for Network Payload Serialization and Parsing: Components Handling Network Payloads Encapsulate Jsonobject

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all network integration components and external service adapters.

### Rules

- **R-ORGJSON-001** SHOULD: Components handling network payloads SHOULD encapsulate JSONObject and JSONArray transformations within dedicated client adapters, returning strongly typed domain contracts to internal consumers.
- **R-ORGJSON-002** MANDATORY: The consumer MUST discover the project dependency manifest, build tool, and lock/resolution artifact to ground dependency versions before writing code that uses a versioned library.
- **R-ORGJSON-003** MANDATORY: Construct request bodies using JSONObject and populate array properties using JSONArray before serializing to network streams.
- **R-ORGJSON-004** MANDATORY: Isolate all org.json references within the integration boundary to keep domain models decoupled from raw JSON primitives.

### Verify

```bash
# Discover and run the project dependency verification command to validate library resolution.
# Discover and run the project static analysis and test suite to ensure all network integration tests pass.
```

**Accept when:**
- All unit and integration tests covering network clients pass successfully.
- Static analysis confirms no unauthorized JSON parsing libraries are introduced in integration modules.

<enforcement>
Claude Code MUST NOT skip or defer verification. Automated continuous integration build checks and peer code reviews enforce compliance.
</enforcement>