---
version: 2
name: cross-cutting-trader-subcomponents-not-introduce-redun-456a
description: "Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Trader Subcomponents Not Introduce Redundant Divergent"
metadata:
  actual:
    adr_id: 456ab13b-0acd-4a4f-a4e0-9e34146b9e52
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

# Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Trader Subcomponents Not Introduce Redundant Divergent

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-TRADER-001** MUST_NOT: Trader subcomponents MUST NOT introduce redundant or divergent intermediate time-series price models for internal processing.

### Verify

```bash
# Discover and execute project compilation and static analysis suite
# Discover and execute project automated test suite covering indicator calculations, strategy voting, and execution engine workflows
```

**Accept when:**
- All trader components compile successfully without unresolved references or type mismatches against com.codeassist.agenttrader.bitunix.Candle.
- Automated test suites pass, confirming strategy voting, indicator computation, and signal generation correctly process sequences of com.codeassist.agenttrader.bitunix.Candle.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>