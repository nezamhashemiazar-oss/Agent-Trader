---
version: 2
name: cross-cutting-strategy-evaluation-methods-not-accept-7fb7
description: "Strategy Protocol and StrategyRegistry Contract: Strategy Evaluation Methods Not Accept Arbitrary"
metadata:
  actual:
    adr_id: 7fb75746-e1fa-4129-a7da-cecf4cad7c41
    status: accepted
    candidate_group_id: d8480ea9-60bc-4e49-a904-62468005c170
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Strategy Protocol and StrategyRegistry Contract: Strategy Evaluation Methods Not Accept Arbitrary

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-STRAT-001** MUST_NOT: Strategy evaluation methods MUST NOT accept arbitrary mutable state outside of the provided Candle sequence and encapsulated Indicators.

### Verify

```bash
# Discover and run the project static analysis and contract inspection scripts to confirm that all classes implementing Strategy adhere to the vote method signature.
# Discover and execute the project automated test suite to validate that registered strategies in StrategyRegistry produce valid Vote outcomes when supplied with test Candle sequences.
```

**Accept when:**
- All strategy implementations strictly implement the Strategy interface and compile without contract discrepancies.
- The automated test suite passes with all registered strategies successfully producing Vote data contracts when evaluated.

<enforcement>
Claude Code MUST NOT skip or defer verification. All strategy classes must adhere to the Strategy protocol and StrategyRegistry requirements.
</enforcement>