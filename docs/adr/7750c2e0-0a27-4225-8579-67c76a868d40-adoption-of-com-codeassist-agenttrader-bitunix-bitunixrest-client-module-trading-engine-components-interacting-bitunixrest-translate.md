---
version: 2
name: 7750c2e0-0a27-4225-8579-67c76a868d40-adoption-of-com-codeassist-agenttrader-bitunix-bitunixrest-client-module-trading-engine-components-interacting-bitunixrest-translate
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Engine Components Interacting Bitunixrest Translate"
metadata:
  actual:
    adr_id: 7750c2e0-0a27-4225-8579-67c76a868d40
    status: accepted
    candidate_group_id: d88fa197-1133-4662-87ac-0060a1755fe6
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Engine Components Interacting Bitunixrest Translate

Status: proposed
Date: 2025-05-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`
- `src/main/kotlin/com/codeassist/agenttrader/trader/**`

## Context

- Trading engine execution components require structured access to market data candles and exchange interaction primitives without coupling trading strategies to low-level transport mechanisms.
- The codebase standardizes exchange interactions through the internal com.codeassist.agenttrader.bitunix.BitunixRest client and associated Candle data structures.
- Trading engine components consume Candle records and invoke BitunixRest endpoints while exposing Position domain contracts to downstream components.

## Problem Statement

Directly embedding network transport logic, endpoint serialization, and vendor-specific authentication within trading engine execution loops creates tight coupling to external exchange interfaces, impedes testability of position calculations, and disperses market data normalization logic across engine components.

## Decision

1. MUST: Trading engine components interacting with BitunixRest MUST translate exchange-specific order and execution payloads into internal Position contracts before publishing state changes to dependent subsystems.

## Policy Block

- MUST Trading engine components interacting with BitunixRest MUST translate exchange-specific order and execution payloads into internal Position contracts before publishing state changes to dependent subsystems.

In scope:
- Trading engine execution components responsible for market data ingestion and order management with Bitunix exchange interfaces.
- Internal modules managing exchange connectivity and Candle data contracts.

Out of scope:
- Trading strategies operating entirely over pre-computed or offline backtesting datasets without live exchange connectivity.
- Alternative exchange adapter modules that do not interface with the Bitunix platform.

## Rationale

- Encapsulating Bitunix connectivity within com.codeassist.agenttrader.bitunix.BitunixRest centralizes authentication, endpoint formatting, and rate-limiting within a dedicated client boundary.
- Consuming standardized Candle structures establishes deterministic market data schemas across trading engine execution paths.
- Enforcing boundaries between raw BitunixRest calls and public Position contracts isolates trading state logic from upstream API contract modifications.

## Consequences

Positive:
- Centralized exchange communication simplifies auditing and maintenance of Bitunix endpoint interactions.
- Uniform Candle model usage avoids divergent market data representations across trading engine modules.
- Strict mapping to internal Position contracts shields core engine business logic from external exchange API schema shifts.

Negative:
- Engine modules depend directly on com.codeassist.agenttrader.bitunix module contracts, requiring synchronized updates when exchange schemas evolve.
- Additional translation overhead is incurred when mapping between BitunixRest responses, Candle records, and domain Position contracts.

## Alternatives

- Embedded inline HTTP transport calls within trading engine execution logic (rejected)
  Rejected because: Scattering network protocol handling, query signing, and serialization across the engine creates brittle dependencies and prevents isolated unit testing of trading strategies.
  When valid: Disposable prototypes or single-file test harnesses where architectural isolation is deliberately deprioritized.
- Generic multi-exchange abstraction layer spanning all supported brokerages (deferred)
  Rejected because: Prematurely abstracting disparate exchange semantics introduces high interface complexity before multiple exchange requirements are proven in the codebase.
  When valid: Active integration of three or more concurrent cryptocurrency exchanges with identical execution features.

## Risks

- Breaking API changes from the upstream Bitunix REST service can invalidate the BitunixRest client implementation.
  Mitigation: Encapsulate endpoint mapping within the dedicated client module and validate contracts against published exchange specifications.
  Owner: engineering team
- Network latency or transport failures in BitunixRest calls during execution loops can delay Position state updates.
  Mitigation: Incorporate non-blocking execution semantics, timeouts, and error-handling policies around client invocations.
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
- Construct instances of BitunixRest via dependency injection or factory providers rather than hardcoding client instantiation inside execution loop routines.
- Process Candle streams sequentially and ensure that updates to Position contracts are atomic with respect to engine state changes.

## Continuation Context


Verify commands:
- Discover and execute the repository compilation command to ensure that all references to com.codeassist.agenttrader.bitunix.BitunixRest and Candle adhere to defined module boundaries.
- Discover and execute the repository test suite to verify that engine state transitions correctly produce valid Position instances upon processing exchange responses.

Accept when:
- The project build succeeds with all trading engine components referencing BitunixRest through the designated client module without circular dependencies.
- Automated test suites pass, verifying that Candle ingestion and Position generation conform to expected business contracts.

## Enforcement

- Verified by: Automated continuous integration checks that compile code and execute test suites.
- Verified by: Architectural boundary reviews during peer code reviews to ensure exchange communication remains confined to designated client modules.
- Violation handling: Build failure on automated continuous integration checks when module boundary rules or contract mappings are breached.
- Violation handling: Mandatory remediation and review approval prior to merging changes that bypass client contracts.
- Exception process: Submit an architectural change request with technical justification for bypassing BitunixRest or Candle models to lead maintainers.
- Exception process: Obtain formal maintainer approval and document the temporary exception with an explicit sunset plan.