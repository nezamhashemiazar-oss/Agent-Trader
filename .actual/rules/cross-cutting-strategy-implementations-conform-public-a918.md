---
version: 2
name: cross-cutting-strategy-implementations-conform-public-a918
description: "Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Strategy Implementations Conform Public Contract Protocol"
metadata:
  actual:
    adr_id: a918bad4-4293-46a1-bc35-2e9f4a544944
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

# Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Strategy Implementations Conform Public Contract Protocol

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for components implementing trading strategies that evaluate market candle series and modules registering strategy implementations with StrategyRegistry.

### Rules

- **R-IND-001** MUST: Strategy implementations MUST conform to the public contract protocol fun vote(c: List<Candle>): Vote and return a structured Vote token.
- **R-IND-002** MUST: Utilize the com.codeassist.agenttrader.trader.Indicators module for computing technical indicators within trading strategies, avoiding inline or duplicate calculations.
- **R-IND-003** MUST: Register newly defined strategy instances with StrategyRegistry using the project's dependency discovery and initialization convention.

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