---
version: 2
name: 85d9cd44-5b51-44a9-94cf-1b2d75d50846-adoption-of-com-codeassist-agenttrader-trader-indicators-module-for-trading-strategy-evaluation-consumer-inspect-dependency-lock-artifact-project
description: "Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Consumer Inspect Dependency Lock Artifact Project"
metadata:
  actual:
    adr_id: 85d9cd44-5b51-44a9-94cf-1b2d75d50846
    status: accepted
    candidate_group_id: 40bfbe3c-8eaf-4f5a-8a90-6011e504357a
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Consumer Inspect Dependency Lock Artifact Project

Status: proposed
Date: 2025-05-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`
- `src/main/kotlin/com/codeassist/agenttrader/trader/**`

## Context

- Trading strategies require mathematical indicator computations over historical and streaming market candle series to evaluate market conditions.
- Directly embedding indicator calculations inside individual strategy definitions creates duplicate logic, inconsistent mathematical calculations, and fragmented testing surfaces.
- The codebase establishes a clear separation by encapsulating indicator logic within com.codeassist.agenttrader.trader.Indicators while standardizing strategy execution through Strategy and StrategyRegistry contracts operating over market Candle inputs.

## Problem Statement

Algorithmic trading systems require a consistent mechanism for computing technical indicators while allowing diverse trading strategies to cast execution votes without duplicating calculation logic or coupling strategies to specific exchange protocols.

## Decision

1. MUST: The consumer MUST inspect the dependency lock artifact in the project repository to verify the resolved version of all supporting libraries before compiling or executing strategy implementations.

## Policy Block

- MUST The consumer MUST inspect the dependency lock artifact in the project repository to verify the resolved version of all supporting libraries before compiling or executing strategy implementations.

In scope:
- Components implementing trading strategies that evaluate market candle series.
- Modules registering strategy implementations with StrategyRegistry.

Out of scope:
- Raw market data ingestion and exchange transport handlers that do not evaluate trading decisions.
- Ad-hoc utility scripts operating outside the automated trading engine.

## Rationale

- Centralizing calculation routines within com.codeassist.agenttrader.trader.Indicators guarantees mathematical consistency and centralized validation across all trading strategies.
- Enforcing the fun vote(c: List<Candle>): Vote protocol ensures that all strategies provide a uniform invocation contract for the execution engine.
- Registering strategies in StrategyRegistry decouples strategy selection and orchestration from individual strategy calculation logic.

## Consequences

Positive:
- Eliminates redundant technical indicator math across disparate trading strategies.
- Enforces a consistent voting interface that simplifies strategy orchestration and composite decision making.
- Facilitates isolated unit testing of indicator calculations independently from trade execution logic.

Negative:
- Strategies remain coupled to the internal data representations and calculation signatures exposed by com.codeassist.agenttrader.trader.Indicators.
- Adding bespoke indicators requires modifying or extending the shared indicator module rather than executing private in-strategy math.

## Alternatives

- Inline indicator calculation within each individual strategy (rejected)
  Rejected because: Results in widespread code duplication, mathematical drift across strategies, and increased test maintenance overhead.
  When valid: Acceptable only in rapid single-file prototyping where indicators are never reused across strategy components.
- Externalizing indicator computation to third-party microservices (rejected)
  Rejected because: Introduces unnecessary network latency and serialization overhead into real-time strategy voting loops.
  When valid: Valid when indicator computations require distributed high-performance computing clusters beyond local memory capacity.

## Risks

- Performance degradation if complex indicator calculations are re-evaluated redundantly across multiple strategies processing identical candle batches.
  Mitigation: Implement caching or pre-computation of shared indicator values across concurrent strategy evaluations within the registry.
  Owner: Trading Engine Engineering Team
- Breaking changes in the com.codeassist.agenttrader.trader.Indicators interface disrupt all registered strategy implementations.
  Mitigation: Enforce strict semantic versioning and regression test suites across all registered strategy vote contracts.
  Owner: Core Platform Team

## Implementation Notes

- DISCOVERY POLICY (MANDATORY): This ADR omits all tool names, file names, commands, package managers, and version numbers. The consumer MUST derive them from the project repository.

LOCK-VERSION GROUNDING (MANDATORY) — before writing code that uses a versioned library, execute in order:
1. Find the dependency manifest in the repo. It declares ranges, not installed versions.
2. Identify the build tool from the manifest.
3. Inspect the repository lock or resolution artifact to determine the exact resolved version. This artifact is authoritative; build-tool output only verifies the active environment matches it.
4. Look up the official documentation, changelog, or public API reference for that exact version. Do not use training-data recall — fetch or search the public internet for version-specific docs.
5. Confirm every API, class, or function you will call exists in that exact version's documentation before using it.
6. For version-sensitive behavior, re-run steps 3-5 per dependency at point of use.
- Discover the existing implementations of Strategy within the repository to inspect how Indicators methods are invoked during the execution of fun vote(c: List<Candle>): Vote.
- Ensure newly defined strategies register their instances with StrategyRegistry using the project's dependency discovery and initialization convention.

## Continuation Context


Verify commands:
- Discover and execute the project compilation script to verify all Strategy implementations cleanly bind to com.codeassist.agenttrader.trader.Indicators.
- Discover and run the project unit test suite targeting strategy voting and indicator calculation assertions.

Accept when:
- All strategy implementations cleanly compile against the Strategy contract and com.codeassist.agenttrader.trader.Indicators module.
- Unit tests validate that fun vote(c: List<Candle>): Vote returns deterministic Vote instances when supplied with valid Candle lists.
- Strategy implementations register with StrategyRegistry without initialization errors.

## Enforcement

- Verified by: Automated continuous integration build checks verifying interface adherence and unit test coverage.
- Verified by: Peer code review auditing strategy implementations for absence of inline indicator calculations.
- Violation handling: Pull requests containing inline technical calculations or bypassing com.codeassist.agenttrader.trader.Indicators must be rejected until refactored.
- Violation handling: Strategies failing to implement the fun vote(c: List<Candle>): Vote protocol are blocked by compiler errors.
- Exception process: Submit an architectural review request detailing why an exploratory indicator cannot be incorporated into com.codeassist.agenttrader.trader.Indicators before implementation.