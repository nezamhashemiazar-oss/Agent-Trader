---
version: 2
name: cross-cutting-domain-modules-derive-specialized-mathem-6ff5
description: "Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Domain Modules Derive Specialized Mathematical Structures"
metadata:
  actual:
    adr_id: 6ff561d4-ba14-48fe-809a-f6e4b0e2532d
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

# Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Domain Modules Derive Specialized Mathematical Structures

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all trading strategy evaluation routines, indicator calculations, signal planning modules, and execution engine components within the trader domain.

### Rules

- **R-CANDLE-001** MAY: Domain modules MAY derive specialized mathematical structures such as indicator bands and pattern contracts from com.codeassist.agenttrader.bitunix.Candle inputs without mutating the underlying market data instances.

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