---
version: 2
name: cross-cutting-active-strategy-instances-register-strat-122b
description: "Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Active Strategy Instances Register Strategyregistry Lifecycle"
metadata:
  actual:
    adr_id: 122b83ee-8994-43ac-b532-03b80e4eecf2
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

# Adoption of com.codeassist.agenttrader.trader.Indicators Module for Trading Strategy Evaluation: Active Strategy Instances Register Strategyregistry Lifecycle

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-IND-001** MUST: All active Strategy instances MUST register with StrategyRegistry for lifecycle and execution coordination.

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