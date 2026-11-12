---
version: 2
name: cross-cutting-strategy-implementations-not-perform-inl-b963
description: "Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Strategy Implementations Not Perform Inline Mathematical"
metadata:
  actual:
    adr_id: b963215f-1532-426b-90df-31565bf1a716
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

# Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Strategy Implementations Not Perform Inline Mathematical

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for components implementing trading strategies that evaluate market candle series and modules registering strategy implementations with StrategyRegistry.

### Rules

- **R-IND-001** MUST_NOT: Strategy implementations MUST NOT perform inline mathematical indicator derivations or duplicate indicator calculations outside the com.codeassist.agenttrader.trader.Indicators module.

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
Claude Code MUST NOT skip or defer verification.
</enforcement>