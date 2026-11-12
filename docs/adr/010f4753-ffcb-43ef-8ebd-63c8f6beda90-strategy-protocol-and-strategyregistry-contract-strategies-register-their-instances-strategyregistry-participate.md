---
version: 2
name: 010f4753-ffcb-43ef-8ebd-63c8f6beda90-strategy-protocol-and-strategyregistry-contract-strategies-register-their-instances-strategyregistry-participate
description: "Strategy Protocol and StrategyRegistry Contract: Strategies Register Their Instances Strategyregistry Participate"
metadata:
  actual:
    adr_id: 010f4753-ffcb-43ef-8ebd-63c8f6beda90
    status: accepted
    candidate_group_id: d8480ea9-60bc-4e49-a904-62468005c170
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Strategy Protocol and StrategyRegistry Contract: Strategies Register Their Instances Strategyregistry Participate

Status: proposed
Date: 2025-05-15
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`
- `src/main/kotlin/com/codeassist/agenttrader/trader/**`

## Context

- Trading evaluation mechanisms require consistent interaction contracts across varying mathematical and market indicator algorithms.
- Trading strategies require ingestion of market candle sequences while outputting standardized voting decisions to drive order generation.
- Strategy registration and lifecycle management are centralized through a shared registry abstraction to facilitate dynamic dispatch and composition.

## Problem Statement

Without a standardized protocol for trading evaluation, diverse algorithmic strategies risk exposing inconsistent method signatures, coupling evaluation logic to specific execution pipelines, and preventing uniform aggregation of decision votes from market history.

## Decision

1. MUST: Strategies MUST register their instances with StrategyRegistry to participate in market decision aggregation.

## Policy Block

- MUST Strategies MUST register their instances with StrategyRegistry to participate in market decision aggregation.

In scope:
- Implementation of trading decision strategies that analyze market price histories.
- Components integrating with strategy dispatch and registration mechanisms.

Out of scope:
- Low-level network communication and exchange connectivity protocols.
- Direct order execution engines operating post-vote consensus.

Exceptions:
- EXC-25-001: A specialized strategy requires real-time order-book delta feeds not captured within sequential market candle data

## Rationale

- The fun vote(c: List<Candle>): Vote method contract enforces a functional, deterministic transformation from historical price data to voting decisions.
- Centralizing discovery through StrategyRegistry decouples execution runners from concrete strategy implementations, enabling dynamic composition and modular testing.
- Standardizing on the Vote data contract ensures that downstream decision aggregation layers can consume signals uniformly without type-specific branching.

## Consequences

Positive:
- Enforces deterministic and testable strategy evaluation across diverse indicator models.
- Simplifies addition and replacement of trading algorithms via StrategyRegistry integration.
- Standardizes downstream signal processing by constraining output to the Vote contract.

Negative:
- Restricts strategy decision inputs strictly to sequential candle lists, limiting direct access to tick-level microstructure.
- Adds interface adherence overhead for single-indicator algorithms.

## Alternatives

- Ad-hoc strategy classes exposing custom execution methods without a shared protocol (rejected)
  Rejected because: Custom method signatures prevent uniform dynamic dispatch, increase coupling in execution runners, and eliminate standardized voting aggregation.
  When valid: In non-voting standalone evaluation harnesses with a single fixed trading rule.
- Event-driven asynchronous message bus dispatching raw tick events directly to individual strategy handlers (rejected)
  Rejected because: Event streams introduce non-deterministic state management and complex synchronization across concurrent strategies compared to pure candle evaluation.
  When valid: In ultra-low-latency trading architectures requiring sub-millisecond execution over raw exchange feeds.

## Risks

- Strategies performing heavy computational processing within the synchronous vote method may introduce evaluation latency.
  Mitigation: Encapsulate heavy mathematical transformations within shared Indicators and establish evaluation timeouts in the execution harness.
  Owner: Trading Systems Engineering
- Incomplete candle histories passed to the vote protocol could lead to unhandled edge cases in indicator calculation.
  Mitigation: Validate candle list minimum length requirements before invocation and return neutral Vote instances when data is insufficient.
  Owner: Market Data and Strategy Engineering

## Implementation Notes

- DISCOVERY POLICY (MANDATORY): This ADR omits all tool names, file names, commands, package managers, and version numbers. The consumer MUST derive them from the project repository.

LOCK-VERSION GROUNDING (MANDATORY) — before writing code that uses a versioned library, execute in order:
1. Find the dependency manifest in the repo. It declares ranges, not installed versions.
2. Identify the build tool from the manifest.
3. Inspect the repository lock or resolution artifact to determine the exact resolved version. This artifact is authoritative; build-tool output only verifies the active environment matches it.
4. Look up the official documentation, changelog, or public API reference for that exact version. Do not use training-data recall — fetch or search the public internet for version-specific docs.
5. Confirm every API, class, or function you will call exists in that exact version's documentation before using it.
6. For version-sensitive behavior, re-run steps 3-5 per dependency at point of use.
- Implementations of Strategy must remain stateless across invocations of the vote method, relying strictly on the supplied Candle sequence and internal Indicators.
- New strategy components must register their instances with StrategyRegistry during application bootstrap to ensure availability to the consensus coordinator.

## Continuation Context


Verify commands:
- Discover and run the project static analysis and contract inspection scripts to confirm that all classes implementing Strategy adhere to the vote method signature.
- Discover and execute the project automated test suite to validate that registered strategies in StrategyRegistry produce valid Vote outcomes when supplied with test Candle sequences.

Accept when:
- All strategy implementations strictly implement the Strategy interface and compile without contract discrepancies.
- The automated test suite passes with all registered strategies successfully producing Vote data contracts when evaluated.

## Enforcement

- Verified by: Automated continuous integration checks validating interface implementation compliance.
- Verified by: Peer code review confirming proper registration within StrategyRegistry.
- Violation handling: Pull requests containing strategy classes that bypass the Strategy protocol or StrategyRegistry are blocked from merging.
- Exception process: Architectural exceptions require approval from the technical architecture group with documented justification in an architecture variance record.