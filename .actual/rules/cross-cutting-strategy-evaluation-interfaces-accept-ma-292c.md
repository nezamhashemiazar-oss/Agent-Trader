---
version: 2
name: cross-cutting-strategy-evaluation-interfaces-accept-ma-292c
description: "Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Strategy Evaluation Interfaces Accept Market Histories"
metadata:
  actual:
    adr_id: 292ce6c6-7241-45b8-84bd-71bd70ee415e
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

# Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Strategy Evaluation Interfaces Accept Market Histories

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all trading strategy evaluation routines, indicator calculations, signal planning modules, and execution engine components within the trader domain.

### Rules

- **R-TRAD-001** MUST: Strategy evaluation interfaces MUST accept market histories structured as collections of com.codeassist.agenttrader.bitunix.Candle instances.

### Verify

```bash
# Discover and execute the project compilation and static analysis suite to verify type contracts and dependency imports.
# Discover and execute the project automated test suite covering indicator calculations, strategy voting, and execution engine workflows.
```

**Accept when:**
- All trader components compile successfully without unresolved references or type mismatches against com.codeassist.agenttrader.bitunix.Candle.
- Automated test suites pass, confirming strategy voting, indicator computation, and signal generation correctly process sequences of com.codeassist.agenttrader.bitunix.Candle.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>