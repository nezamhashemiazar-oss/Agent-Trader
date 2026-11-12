---
version: 2
name: 4e8a6d07-201b-4650-8d97-542bd53937ed-okhttp-websocket-client-adoption-for-real-time-streaming-real-time-full-duplex-socket-communication
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

Status: proposed
Date: 2025-02-16
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/bitunix/**`
- `src/main/kotlin/com/codeassist/agenttrader/bitunix/**`

## Context

- Real-time data ingestion and exchange integrations require resilient, full-duplex WebSocket connections for bidirectional event streaming.
- Ad-hoc network client implementations lead to fragmented connection handling, inconsistent reconnect logic, and disparate threading models.
- The project adopts OkHttp (OkHttpClient, Request, Response, WebSocket, WebSocketListener) to standardize WebSocket lifecycle management and message event handling across streaming modules.

## Problem Statement

Real-time streaming integration requires a reliable, standard network transport client to handle WebSocket lifecycles, protocol negotiation, and asynchronous event callbacks without duplicating connection management or introducing fragmented networking dependencies across integration modules.

## Decision

1. MUST: Real-time full-duplex socket communication MUST adopt OkHttp (OkHttpClient, Request, Response, WebSocket, and WebSocketListener) for establishing network connections and processing message streams.

## Policy Block

- MUST Real-time full-duplex socket communication MUST adopt OkHttp (OkHttpClient, Request, Response, WebSocket, and WebSocketListener) for establishing network connections and processing message streams.

In scope:
- All modules establishing and maintaining real-time full-duplex WebSocket streaming connections.
- Integration components managing external exchange or service event streams via network sockets.

Out of scope:
- Stateless, synchronous request-response communication where HTTP REST semantics are sufficient.
- Local inter-process communication mechanisms that do not use network sockets.

## Rationale

- OkHttp provides a mature, battle-tested HTTP and WebSocket client abstraction with native support for full-duplex socket streaming, connection health monitoring, and framing.
- Utilizing WebSocketListener establishes a consistent, event-driven contract for message arrival, connection failure, and termination across all streaming components.
- Centralizing network transport on OkHttpClient allows uniform configuration of timeouts, connection pooling, and interceptor-based security controls.

## Consequences

Positive:
- Provides standardized, production-tested WebSocket connection management, protocol negotiation, and framing.
- Decouples network socket transport from domain consumption through clean WebSocketListener and domain listener contract separation.
- Enables connection pooling and shared thread scheduling across network client components.

Negative:
- Introduces runtime architectural coupling to OkHttp connection handling, thread management, and listener callback execution semantics.
- Requires disciplined error handling within WebSocketListener callbacks to prevent silent background failure of streaming data pipelines.

## Alternatives

- Raw socket implementations using runtime standard library network primitives (rejected)
  Rejected because: Requires manual framing, ping-pong heartbeat orchestration, reconnect loops, and TLS handshake management, significantly increasing surface area for protocol defects.
  When valid: Valid only in embedded runtime environments with strict zero-dependency footprint constraints.
- Third-party reactive abstraction wrappers around lower-level network clients (rejected)
  Rejected because: Adds unnecessary architectural indirection and transitive dependency overhead when direct callback listener patterns satisfy streaming requirements.
  When valid: Valid when the entire surrounding application architecture is strictly structured around uniform reactive stream operators.

## Risks

- Unhandled network disconnection or transport errors causing silent stoppage of stream ingestion.
  Mitigation: Enforce proper error dispatching and reconnection triggers within WebSocketListener onFailure callbacks.
  Owner: engineering team
- Resource leakage and thread exhaustion resulting from unmanaged instantiation of multiple OkHttpClient instances.
  Mitigation: Ensure client instances are created as long-lived singletons rather than re-instantiated per connection attempt.
  Owner: engineering team

## Implementation Notes

- DISCOVERY POLICY (MANDATORY): This ADR omits all tool names, file names, commands, package managers, and version numbers. The consumer MUST derive them from the project repository.

LOCK-VERSION GROUNDING (MANDATORY) — before writing code that uses a versioned library, execute in order:
1. Find the dependency manifest in the repo. It declares ranges, not installed versions.
2. Identify the build tool from the manifest.
3. Inspect the repository lock or resolution artifact to determine the exact resolved version. This artifact is authoritative; build-tool output only verifies the active environment matches it.
4. Look up the official documentation, changelog, or public API reference for that exact version. Do not use training-data recall — fetch or search the public internet for version-specific docs.
5. Confirm every API, class, or function you will call exists in that exact version's documentation before using it.
6. For version-sensitive behavior, re-run steps 3-5 per dependency at point of use.
- Instantiate OkHttpClient instances as long-lived singletons or shared components to maximize connection reuse and prevent thread pool exhaustion.
- Map WebSocketListener callback events cleanly to domain contract interfaces to prevent leaking transport-layer abstractions into downstream business logic.

## Continuation Context


Verify commands:
- Discover the project build and test automation scripts from repository manifests and execute the test suite validating WebSocket client connectivity and listener behavior.
- Inspect repository static analysis and dependency validation configurations to verify that OkHttp dependency versions conform to the authoritative lock resolution artifact.

Accept when:
- All unit and integration tests exercising WebSocket connections and listener event callbacks execute and pass.
- Dependency auditing confirms that OkHttp is resolved strictly via the authoritative lock artifact without unmanaged transitive overrides.

## Enforcement

- Verified by: Automated build pipeline dependency analysis and static analysis checking import boundaries.
- Verified by: Peer code review validating adherence to WebSocketListener contracts and resource lifecycle constraints.
- Violation handling: Flag unauthorized network client adoptions during static analysis and code review.
- Violation handling: Block pull requests that introduce conflicting WebSocket transport dependencies or bypass established listener contracts.
- Exception process: Submit an architecture revision request detailing why the standard client cannot support the protocol requirements.
- Exception process: Obtain formal approval from the lead architecture authority prior to introducing alternative network transport libraries.