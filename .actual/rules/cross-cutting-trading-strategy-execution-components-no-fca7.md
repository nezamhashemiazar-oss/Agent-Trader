---
version: 2
name: cross-cutting-trading-strategy-execution-components-no-fca7
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Strategy Execution Components Not Import"
metadata:
  actual:
    adr_id: fca71797-7c87-4921-b527-28792e1e1311
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

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Strategy Execution Components Not Import

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for trading engine execution components responsible for market data ingestion and order management with Bitunix exchange interfaces, and internal modules managing exchange connectivity and Candle data contracts.

### Rules

- **R-BITUNIX-001** MUST_NOT: Trading strategy and execution components MUST_NOT import or utilize transport-level network clients or exchange-specific response schemas outside of the com.codeassist.agenttrader.bitunix module boundary.

### Verify

```bash
# Discover and execute the repository compilation command to ensure that all references to com.codeassist.agenttrader.bitunix.BitunixRest and Candle adhere to defined module boundaries.
# Discover and execute the repository test suite to verify that engine state transitions correctly produce valid Position instances upon processing exchange responses.
```

**Accept when:**
- The project build succeeds with all trading engine components referencing BitunixRest through the designated client module without circular dependencies.
- Automated test suites pass, verifying that Candle ingestion and Position generation conform to expected business contracts.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>