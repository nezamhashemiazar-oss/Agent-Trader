---
version: 2
name: cross-cutting-trading-engine-components-isolate-bituni-0f9a
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Engine Components Isolate Bitunixrest Client"
metadata:
  actual:
    adr_id: 0f9a084e-a766-42ef-b040-81fed731673f
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

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Engine Components Isolate Bitunixrest Client

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for trading engine execution components responsible for market data ingestion, order management with Bitunix exchange interfaces, and internal modules managing exchange connectivity and Candle data contracts.

### Rules

- **R-BITUNIX-001** SHOULD: Trading engine components isolate BitunixRest client interactions behind dependency-injected interfaces to facilitate isolated unit testing of execution logic and Position state transitions.
- **R-BITUNIX-002** MANDATORY: The consumer MUST discover dependency manifests, build tools, lock files, and authoritative version specifications from the project repository before using version-sensitive library features.
- **R-BITUNIX-003** MANDATORY: Construct instances of BitunixRest via dependency injection or factory providers rather than hardcoding client instantiation inside execution loop routines.
- **R-BITUNIX-004** MANDATORY: Process Candle streams sequentially and ensure that updates to Position contracts are atomic with respect to engine state changes.

### Verify

```bash
# Discover and execute the repository compilation command to verify module boundaries
# Discover and execute the repository test suite to verify engine state transitions and Position instances
```

**Accept when:**
- The project build succeeds with all trading engine components referencing BitunixRest through the designated client module without circular dependencies.
- Automated test suites pass, verifying that Candle ingestion and Position generation conform to expected business contracts.

<enforcement>
Claude Code MUST NOT skip or defer verification. Violation handling results in build failures on automated CI checks and requires mandatory remediation prior to merging changes that bypass client contracts.
</enforcement>