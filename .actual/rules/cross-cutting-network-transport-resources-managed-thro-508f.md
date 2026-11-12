---
version: 2
name: cross-cutting-network-transport-resources-managed-thro-508f
description: "OkHttp WebSocket Client Adoption for Real-Time Streaming: Network Transport Resources Managed Through Shared"
metadata:
  actual:
    adr_id: 508f6331-e67b-4564-848e-87085969f94e
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

# OkHttp WebSocket Client Adoption for Real-Time Streaming: Network Transport Resources Managed Through Shared

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**`, `src/main/kotlin/com/codeassist/agenttrader/bitunix/**`.

These rules are ALWAYS ACTIVE for all modules establishing and maintaining real-time full-duplex WebSocket streaming connections and integration components managing external exchange or service event streams via network sockets.

### Rules

- **R-OKHTTP-001** SHOULD: Network transport resources SHOULD be managed through shared OkHttpClient instances to optimize connection pooling and thread execution.
- **R-OKHTTP-002** MANDATORY: Instantiate OkHttpClient instances as long-lived singletons or shared components to maximize connection reuse and prevent thread pool exhaustion.
- **R-OKHTTP-003** MANDATORY: Map WebSocketListener callback events cleanly to domain contract interfaces to prevent leaking transport-layer abstractions into downstream business logic.
- **R-OKHTTP-004** MANDATORY: Enforce proper error dispatching and reconnection triggers within WebSocketListener onFailure callbacks to prevent silent stoppage of stream ingestion.

### Verify

```bash
# Discover project build/test automation scripts from repository manifests and execute the test suite
# validating WebSocket client connectivity and listener behavior.
# Inspect repository static analysis and dependency validation configurations to verify that OkHttp
# dependency versions conform to the authoritative lock resolution artifact.
```

**Accept when:**
- All unit and integration tests exercising WebSocket connections and listener event callbacks execute and pass.
- Dependency auditing confirms that OkHttp is resolved strictly via the authoritative lock artifact without unmanaged transitive overrides.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verified by automated build pipeline dependency analysis, static analysis checking import boundaries, and peer code review.
</enforcement>