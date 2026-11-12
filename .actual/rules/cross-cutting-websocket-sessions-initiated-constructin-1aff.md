---
version: 2
name: cross-cutting-websocket-sessions-initiated-constructin-1aff
description: "OkHttp WebSocket Client Adoption for Real-Time Streaming: Websocket Sessions Initiated Constructing Request Instances"
metadata:
  actual:
    adr_id: 1aff757b-aac4-4f1f-bcc0-a879ac19f055
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

# OkHttp WebSocket Client Adoption for Real-Time Streaming: Websocket Sessions Initiated Constructing Request Instances

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**`, `src/main/kotlin/com/codeassist/agenttrader/bitunix/**`.

These rules are ALWAYS ACTIVE for all modules establishing and maintaining real-time full-duplex WebSocket streaming connections and integration components managing external exchange or service event streams via network sockets.

### Rules

- **R-WS-001** MUST: WebSocket sessions MUST be initiated by constructing Request instances executed through OkHttpClient to instantiate WebSocket connections.

### Verify

```bash
# Discover the project build and test automation scripts from repository manifests and execute the test suite validating WebSocket client connectivity and listener behavior.
# Inspect repository static analysis and dependency validation configurations to verify that OkHttp dependency versions conform to the authoritative lock resolution artifact.
```

**Accept when:**
- All unit and integration tests exercising WebSocket connections and listener event callbacks execute and pass.
- Dependency auditing confirms that OkHttp is resolved strictly via the authoritative lock artifact without unmanaged transitive overrides.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verification is mandatory via automated build pipeline dependency analysis, static analysis checking import boundaries, and peer code review.
</enforcement>