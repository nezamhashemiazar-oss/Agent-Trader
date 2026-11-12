---
version: 2
name: cross-cutting-before-modifying-extending-modules-that-26ff
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Before Modifying Extending Modules That Depend"
metadata:
  actual:
    adr_id: 26ffb748-d7cd-42f0-8ddd-4546f4cfa557
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

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Before Modifying Extending Modules That Depend

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for trading engine execution components responsible for market data ingestion and order management with Bitunix exchange interfaces, and internal modules managing exchange connectivity and Candle data contracts.

### Rules

- **R-BITUNIX-001** MUST: Before modifying or extending modules that depend on external libraries, developers MUST discover the dependency manifest and inspect the lock or resolution artifact to verify exact dependency versions according to the Discovery Policy.

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