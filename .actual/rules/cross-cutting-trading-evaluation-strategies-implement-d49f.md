---
version: 2
name: cross-cutting-trading-evaluation-strategies-implement-d49f
description: "Strategy Protocol and StrategyRegistry Contract: Trading Evaluation Strategies Implement Public Strategy"
metadata:
  actual:
    adr_id: d49f902f-0588-473e-b805-54482a25ac61
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

# Strategy Protocol and StrategyRegistry Contract: Trading Evaluation Strategies Implement Public Strategy

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-STR-001** MUST: All trading evaluation strategies MUST implement the public Strategy interface exposing the fun vote(c: List<Candle>): Vote contract.
- **R-STR-002** MUST: Implementations of Strategy must remain stateless across invocations of the vote method, relying strictly on the supplied Candle sequence and internal Indicators.
- **R-STR-003** MUST: New strategy components must register their instances with StrategyRegistry during application bootstrap to ensure availability to the consensus coordinator.

### Verify

```bash
# Discover and run the project static analysis and contract inspection scripts to confirm that all classes implementing Strategy adhere to the vote method signature.
# Discover and execute the project automated test suite to validate that registered strategies in StrategyRegistry produce valid Vote outcomes when supplied with test Candle sequences.
```

**Accept when:**
- All strategy implementations strictly implement the Strategy interface and compile without contract discrepancies.
- The automated test suite passes with all registered strategies successfully producing Vote data contracts when evaluated.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>