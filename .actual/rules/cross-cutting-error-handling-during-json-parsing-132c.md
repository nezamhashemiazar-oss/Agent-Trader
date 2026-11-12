---
version: 2
name: cross-cutting-error-handling-during-json-parsing-132c
description: "org.json Adoption for Network Payload Serialization and Parsing: Error Handling During Json Parsing Explicitly"
metadata:
  actual:
    adr_id: 132c86c7-331e-4a12-952b-8dcc2cedadd8
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

# org.json Adoption for Network Payload Serialization and Parsing: Error Handling During Json Parsing Explicitly

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-JSON-001** SHOULD: Error handling during JSON parsing explicitly handle JSONException to prevent unhandled parse failures from propagating across service boundaries.
- **R-JSON-002** MANDATORY: Construct request bodies using JSONObject and populate array properties using JSONArray before serializing to network streams.
- **R-JSON-003** MANDATORY: Isolate all org.json references within the integration boundary to keep domain models decoupled from raw JSON primitives.

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