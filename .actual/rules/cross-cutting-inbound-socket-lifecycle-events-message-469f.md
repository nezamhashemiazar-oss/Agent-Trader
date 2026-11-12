---
version: 2
name: cross-cutting-inbound-socket-lifecycle-events-message-469f
description: "OkHttp WebSocket Client Adoption for Real-Time Streaming: Inbound Socket Lifecycle Events Message Payloads"
metadata:
  actual:
    adr_id: 469f8c2b-f87f-4e7e-8724-a9425dacb343
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

# OkHttp WebSocket Client Adoption for Real-Time Streaming: Inbound Socket Lifecycle Events Message Payloads

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**`, `src/main/kotlin/com/codeassist/agenttrader/bitunix/**`.

These rules are ALWAYS ACTIVE for all modules establishing and maintaining real-time full-duplex WebSocket streaming connections and integration components managing external exchange or service event streams via network sockets.

### Rules

- **R-WS-001** MUST: Inbound socket lifecycle events and message payloads MUST be captured via WebSocketListener implementations and forwarded to application listener contracts.
- **R-WS-002** MUST: Follow the LOCK-VERSION GROUNDING procedure: find dependency manifest, identify build tool, inspect repository lock or resolution artifact for authoritative version, look up official documentation for that exact version, confirm APIs exist, and re-verify per dependency at point of use.
- **R-WS-003** MUST: Instantiate OkHttpClient instances as long-lived singletons or shared components to maximize connection reuse and prevent thread pool exhaustion.
- **R-WS-004** MUST: Map WebSocketListener callback events cleanly to domain contract interfaces to prevent leaking transport-layer abstractions into downstream business logic.
- **R-WS-005** MUST: Enforce proper error dispatching and reconnection triggers within WebSocketListener onFailure callbacks to prevent silent stoppage of stream ingestion.

### Verify

```bash
# Discover the project build and test automation scripts from repository manifests and execute the test suite validating WebSocket client connectivity and listener behavior.
# Inspect repository static analysis and dependency validation configurations to verify that OkHttp dependency versions conform to the authoritative lock resolution artifact.
```

**Accept when:**
- All unit and integration tests exercising WebSocket connections and listener event callbacks execute and pass.
- Dependency auditing confirms that OkHttp is resolved strictly via the authoritative lock artifact without unmanaged transitive overrides.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>