---
version: 2
name: cross-cutting-real-time-full-duplex-socket-4e8a
description: "OkHttp WebSocket Client Adoption for Real-Time Streaming: Real Time Full Duplex Socket Communication"
metadata:
  actual:
    adr_id: 4e8a6d07-201b-4650-8d97-542bd53937ed
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

# OkHttp WebSocket Client Adoption for Real-Time Streaming: Real Time Full Duplex Socket Communication

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**`, `src/main/kotlin/com/codeassist/agenttrader/bitunix/**`.

These rules are ALWAYS ACTIVE for all modules establishing and maintaining real-time full-duplex WebSocket streaming connections and integration components managing external exchange or service event streams via network sockets.

### Rules

- **R-OKHTTP-001** MUST: Real-time full-duplex socket communication MUST adopt OkHttp (OkHttpClient, Request, Response, WebSocket, and WebSocketListener) for establishing network connections and processing message streams.
- **R-OKHTTP-002** MUST: Execute the lock-version grounding process (find manifest, identify build tool, inspect lock/resolution artifact, look up official documentation for the exact version, confirm APIs exist, and re-run per dependency at point of use) before writing code that uses a versioned library.
- **R-OKHTTP-003** MUST: Instantiate OkHttpClient instances as long-lived singletons or shared components to maximize connection reuse and prevent thread pool exhaustion.
- **R-OKHTTP-004** MUST: Map WebSocketListener callback events cleanly to domain contract interfaces to prevent leaking transport-layer abstractions into downstream business logic, and enforce proper error dispatching and reconnection triggers within `onFailure` callbacks to prevent silent background failures.

### Verify

```bash
# Discover and execute project build and test automation scripts to validate WebSocket client connectivity and listener behavior
# Inspect repository static analysis and dependency validation configurations to verify OkHttp dependency versions conform to the authoritative lock resolution artifact
```

**Accept when:**
- All unit and integration tests exercising WebSocket connections and listener event callbacks execute and pass.
- Dependency auditing confirms that OkHttp is resolved strictly via the authoritative lock artifact without unmanaged transitive overrides.

<enforcement>
Claude Code MUST NOT skip or defer verification. Automated build pipeline dependency analysis, static analysis, and peer code review strictly enforce OkHttp adoption, WebSocketListener contracts, and resource lifecycle constraints, blocking pull requests that introduce conflicting transport dependencies or bypass established contracts.
</enforcement>