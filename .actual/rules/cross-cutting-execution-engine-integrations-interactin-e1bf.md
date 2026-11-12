---
version: 2
name: cross-cutting-execution-engine-integrations-interactin-e1bf
description: "Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Execution Engine Integrations Interacting External Market"
metadata:
  actual:
    adr_id: e1bffcf4-8d63-4852-b5e7-354bfea6e4b5
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

# Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Execution Engine Integrations Interacting External Market

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all trading strategy evaluation routines, indicator calculations, signal planning modules, and execution engine components within the trader domain.

### Rules

- **R-CANDLE-001** SHOULD: Execution engine integrations interacting with external market providers SHOULD map raw exchange responses directly into com.codeassist.agenttrader.bitunix.Candle instances before dispatching to downstream analysis components.
- **R-CANDLE-002** MANDATORY: Discover and execute the project compilation and static analysis suite to verify type contracts and dependency imports.
- **R-CANDLE-003** MANDATORY: Discover and execute the project automated test suite covering indicator calculations, strategy voting, and execution engine workflows.
- **R-CANDLE-004** MANDATORY: Maintain immutability of candle instances across concurrent evaluation routines to prevent data races.

### Verify

```bash
# Discover and execute project compilation and static analysis suite
# Discover and execute automated test suite covering indicators, strategy voting, and execution engine workflows
```

**Accept when:**
- All trader components compile successfully without unresolved references or type mismatches against com.codeassist.agenttrader.bitunix.Candle.
- Automated test suites pass, confirming strategy voting, indicator computation, and signal generation correctly process sequences of com.codeassist.agenttrader.bitunix.Candle.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verified by automated static analysis, compilation checks, and peer code review.
</enforcement>