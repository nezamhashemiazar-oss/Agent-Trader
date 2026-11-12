---
version: 2
name: 1a326cbd-c920-4930-b0c4-5d1141adc48b-adoption-of-com-codeassist-agenttrader-bitunix-candle-as-canonical-market-data-representation-trading-domain-components-evaluating-market-data
description: "Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Trading Domain Components Evaluating Market Data"
metadata:
  actual:
    adr_id: 1a326cbd-c920-4930-b0c4-5d1141adc48b
    status: accepted
    candidate_group_id: 3d2f5ae7-e34a-46d3-8cf8-ec6225495130
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Trading Domain Components Evaluating Market Data

Status: proposed
Date: 2025-05-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`
- `src/main/kotlin/com/codeassist/agenttrader/trader/**`

## Context

- Trading systems require structured time-series market price representations across technical indicators, signal generators, strategy evaluators, and execution engines.
- Static analysis indicates that com.codeassist.agenttrader.bitunix.Candle is consistently imported and consumed as the fundamental market data contract across trader modules.
- Strategy voting protocols and indicator calculations require uniform access to historical market price points to ensure reliable trade decisioning.

## Problem Statement

Trading systems processing market data must coordinate price analysis across indicators, strategy voting, and execution components; without a single canonical market data representation, components risk accumulating redundant conversions, type mismatches, and inconsistent price interpretation.

## Decision

1. MUST: All trading domain components evaluating market data MUST consume com.codeassist.agenttrader.bitunix.Candle as the canonical representation for price and volume time-series data.

## Policy Block

- MUST All trading domain components evaluating market data MUST consume com.codeassist.agenttrader.bitunix.Candle as the canonical representation for price and volume time-series data.

In scope:
- All trading strategy evaluation routines, indicator calculations, signal planning modules, and execution engine components within the trader domain.

Out of scope:
- External transport adapters and network-level serializers that operate on raw protocol payloads prior to domain mapping.

## Rationale

- Adopting com.codeassist.agenttrader.bitunix.Candle across the trader domain establishes an unambiguous contract for all market price evaluations.
- Codebase evidence demonstrates uniform usage of Candle in method protocols such as vote and mathematical indicator calculations, proving its role as the shared domain standard.
- A unified time-series representation prevents impedance mismatches between market data ingestion and trading logic execution.

## Consequences

Positive:
- Standardizes market time-series inputs across technical indicators, signal aggregators, and trading strategies.
- Eliminates intermediate data conversion layers and associated translation boilerplate.
- Enforces consistent temporal and price data contracts throughout trading execution pipelines.

Negative:
- Directly couples trader analysis submodules to the upstream market data entity schema.
- Modifications to the candle data model require synchronized updates across all consuming trading, indicator, and signal pipelines.

## Alternatives

- Define independent market data structures within each trader submodule and translate at module boundaries (rejected)
  Rejected because: Introduces redundant mapping overhead, increases memory footprint, and creates unnecessary boilerplate across cohesive trading components
  When valid: When trader submodules must be completely decoupled from any specific upstream market data schema or deployed as independent microservices
- Pass raw primitive arrays for market price histories into strategy and indicator functions (rejected)
  Rejected because: Sacrifices type safety, temporal alignment, and domain clarity for marginal reduction in object references
  When valid: When extreme low-latency constraints dictate zero object allocation in inner calculation loops

## Risks

- Upstream changes to the market data model could ripple across all indicator and strategy implementations.
  Mitigation: Encapsulate candle construction behind structured factory or builder methods to isolate schema adjustments.
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
- Ensure data integrity and ordering when constructing and passing collections of candles into strategy and indicator pipelines.
- Maintain immutability of candle instances across concurrent evaluation routines to prevent data races.

## Continuation Context


Verify commands:
- Discover and execute the project compilation and static analysis suite to verify type contracts and dependency imports.
- Discover and execute the project automated test suite covering indicator calculations, strategy voting, and execution engine workflows.

Accept when:
- All trader components compile successfully without unresolved references or type mismatches against com.codeassist.agenttrader.bitunix.Candle.
- Automated test suites pass, confirming strategy voting, indicator computation, and signal generation correctly process sequences of com.codeassist.agenttrader.bitunix.Candle.

## Enforcement

- Verified by: Automated static analysis and compilation checks verifying dependency boundaries and type signatures.
- Verified by: Peer code review verifying adherence to canonical domain model consumption.
- Violation handling: Build failure upon introduction of unauthorized duplicate market data structures or bypassing canonical contracts.
- Violation handling: Code review rejection for PRs creating divergent intermediate candle representations in trading pipelines.
- Exception process: Submit an architectural review request detailing why an alternative time-series contract or translation layer is necessary for a specialized trading pipeline.