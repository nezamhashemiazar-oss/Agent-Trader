---
version: 2
name: cross-cutting-market-data-ingest-exchange-intervals-897b
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Market Data Ingest Exchange Intervals Consume"
metadata:
  actual:
    adr_id: 897b805b-712f-49f1-b212-767d20f98ef6
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

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Market Data Ingest Exchange Intervals Consume

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-MARKET-001** MUST: Market data ingest for exchange intervals MUST consume com.codeassist.agenttrader.bitunix.Candle data contracts to ensure uniform time-series representations across engine execution loops.

### Verify

```bash
# Discover and execute the repository compilation command to ensure that all references to com.codeassist.agenttrader.bitunix.BitunixRest and Candle adhere to defined module boundaries.
# Discover and execute the repository test suite to verify that engine state transitions correctly produce valid Position instances upon processing exchange responses.
```

**Accept when:**
- The project build succeeds with all trading engine components referencing BitunixRest through the designated client module without circular dependencies.
- Automated test suites pass, verifying that Candle ingestion and Position generation conform to expected business contracts.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verification is enforced via automated continuous integration checks and architectural boundary reviews.
</enforcement>