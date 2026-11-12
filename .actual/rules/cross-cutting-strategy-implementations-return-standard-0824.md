---
version: 2
name: cross-cutting-strategy-implementations-return-standard-0824
description: "Strategy Protocol and StrategyRegistry Contract: Strategy Implementations Return Standardized Vote Data"
metadata:
  actual:
    adr_id: 0824c925-f671-4d15-83ae-786c825fca3f
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

# Strategy Protocol and StrategyRegistry Contract: Strategy Implementations Return Standardized Vote Data

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-STRAT-001** MUST: Strategy implementations MUST return a standardized Vote data contract in response to input sequences of Candle records.

### Verify

```bash
# Discover and run static analysis and contract inspection scripts to confirm Strategy implementation compliance
# Discover and execute automated test suite to validate registered strategies produce valid Vote outcomes
```

**Accept when:**
- All strategy implementations strictly implement the Strategy interface and compile without contract discrepancies.
- The automated test suite passes with all registered strategies successfully producing Vote data contracts when evaluated.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verification is mandatory via automated CI checks and peer code review.
</enforcement>