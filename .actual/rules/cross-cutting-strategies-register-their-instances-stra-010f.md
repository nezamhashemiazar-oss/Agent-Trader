---
version: 2
name: cross-cutting-strategies-register-their-instances-stra-010f
description: "Strategy Protocol and StrategyRegistry Contract: Strategies Register Their Instances Strategyregistry Participate"
metadata:
  actual:
    adr_id: 010f4753-ffcb-43ef-8ebd-63c8f6beda90
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

# Strategy Protocol and StrategyRegistry Contract: Strategies Register Their Instances Strategyregistry Participate

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-STRAT-001** MUST: Strategies MUST register their instances with StrategyRegistry to participate in market decision aggregation.

### Verify

```bash
# Discover and run the project static analysis and contract inspection scripts to confirm that all classes implementing Strategy adhere to the vote method signature.
# Discover and execute the project automated test suite to validate that registered strategies in StrategyRegistry produce valid Vote outcomes when supplied with test Candle sequences.
```

**Accept when:**
- All strategy implementations strictly implement the Strategy interface and compile without contract discrepancies.
- The automated test suite passes with all registered strategies successfully producing Vote data contracts when evaluated.

<enforcement>
Claude Code MUST NOT skip or defer verification. All strategy classes must implement the Strategy protocol and register with StrategyRegistry; pull requests bypassing this are blocked.
</enforcement>