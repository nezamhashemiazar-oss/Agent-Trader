---
version: 2
name: cross-cutting-trading-domain-components-evaluating-mar-1a32
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

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all trading domain components evaluating market data.

### Rules

- **R-TRAD-001** MUST: All trading domain components evaluating market data MUST consume com.codeassist.agenttrader.bitunix.Candle as the canonical representation for price and volume time-series data.

### Verify

```bash
# Discover and execute the project compilation and static analysis suite to verify type contracts and dependency imports.
# Discover and execute the project automated test suite covering indicator calculations, strategy voting, and execution engine workflows.
```

**Accept when:**
- All trader components compile successfully without unresolved references or type mismatches against com.codeassist.agenttrader.bitunix.Candle.
- Automated test suites pass, confirming strategy voting, indicator computation, and signal generation correctly process sequences of com.codeassist.agenttrader.bitunix.Candle.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verification is mandatory.
</enforcement>