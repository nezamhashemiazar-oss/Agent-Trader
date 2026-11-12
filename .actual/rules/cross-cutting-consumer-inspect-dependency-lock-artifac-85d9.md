---
version: 2
name: cross-cutting-consumer-inspect-dependency-lock-artifac-85d9
description: "Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Consumer Inspect Dependency Lock Artifact Project"
metadata:
  actual:
    adr_id: 85d9cd44-5b51-44a9-94cf-1b2d75d50846
    status: accepted
    candidate_group_id: 40bfbe3c-8eaf-4f5a-8a90-6011e504357a
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Consumer Inspect Dependency Lock Artifact Project

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for components implementing trading strategies that evaluate market candle series and modules registering strategy implementations with StrategyRegistry.

### Rules

- **R-IND-001** MUST: The consumer MUST inspect the dependency lock artifact in the project repository to verify the resolved version of all supporting libraries before compiling or executing strategy implementations.
- **R-IND-002** MUST: Centralize all mathematical indicator calculations within the `com.codeassist.agenttrader.trader.Indicators` module; inline technical indicator calculations inside individual strategy definitions are strictly prohibited.
- **R-IND-003** MUST: Implement the standardized strategy execution contract via `Strategy` and `StrategyRegistry` operating over market `Candle` inputs and exposing the uniform `fun vote(c: List<Candle>): Vote` protocol.

### Verify

```bash
# Discover and execute the project compilation script to verify all Strategy implementations cleanly bind to com.codeassist.agenttrader.trader.Indicators.
# Discover and run the project unit test suite targeting strategy voting and indicator calculation assertions.
```

**Accept when:**
- All strategy implementations cleanly compile against the Strategy contract and com.codeassist.agenttrader.trader.Indicators module.
- Unit tests validate that fun vote(c: List<Candle>): Vote returns deterministic Vote instances when supplied with valid Candle lists.
- Strategy implementations register with StrategyRegistry without initialization errors.

<enforcement>
Claude Code MUST NOT skip or defer verification. All pull requests containing inline technical calculations or bypassing com.codeassist.agenttrader.trader.Indicators must be rejected until refactored.
</enforcement>