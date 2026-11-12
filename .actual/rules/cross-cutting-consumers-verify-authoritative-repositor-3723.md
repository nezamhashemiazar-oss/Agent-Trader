---
version: 2
name: cross-cutting-consumers-verify-authoritative-repositor-3723
description: "OkHttp WebSocket Client Adoption for Real-Time Streaming: Consumers Verify Authoritative Repository Dependency Lock"
metadata:
  actual:
    adr_id: 37236340-59a5-40e8-be87-b0a5872b4a56
    status: accepted
    candidate_group_id: 734acd54-2d70-46b8-8e28-67d2e57e3955
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**
      - src/main/kotlin/com/codeassist/agenttrader/bitunix/**
---

# OkHttp WebSocket Client Adoption for Real-Time Streaming: Consumers Verify Authoritative Repository Dependency Lock

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**`, `src/main/kotlin/com/codeassist/agenttrader/bitunix/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-OKHTTP-001** MUST: Consumers MUST verify the authoritative repository dependency lock artifact to determine the exact resolved library version and validate all invoked API signatures against official documentation before implementation.

### Verify

```bash
# Discover project build and test automation scripts from repository manifests and execute tests
# Inspect repository static analysis and dependency validation configurations for OkHttp version conformance
```

**Accept when:**
- All unit and integration tests exercising WebSocket connections and listener event callbacks execute and pass.
- Dependency auditing confirms that OkHttp is resolved strictly via the authoritative lock artifact without unmanaged transitive overrides.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>