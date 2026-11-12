---
version: 2
name: cross-cutting-strategies-compute-analytical-metrics-vi-4d43
description: "Strategy Protocol and StrategyRegistry Contract: Strategies Compute Analytical Metrics Via Indicators"
metadata:
  actual:
    adr_id: 4d437d6e-529f-4ecb-bc2f-1ffb9a48d3a1
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

# Strategy Protocol and StrategyRegistry Contract: Strategies Compute Analytical Metrics Via Indicators

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-STR-001** SHOULD: Strategies SHOULD compute analytical metrics via Indicators before producing a Vote contract.
- **R-STR-002** MANDATORY: Implementations of Strategy must remain stateless across invocations of the vote method, relying strictly on the supplied Candle sequence and internal Indicators.
- **R-STR-003** MANDATORY: New strategy components must register their instances with StrategyRegistry during application bootstrap to ensure availability to the consensus coordinator.

### Verify

```bash
# Discover and run project static analysis, contract inspection, and automated test suite
# (Commands to be derived from the project repository configuration)
```

**Accept when:**
- All strategy implementations strictly implement the Strategy interface and compile without contract discrepancies.
- The automated test suite passes with all registered strategies successfully producing Vote data contracts when evaluated.

<enforcement>
Claude Code MUST NOT skip or defer verification. All strategy classes must adhere to the Strategy protocol, register with StrategyRegistry, and pass automated continuous integration and peer code review checks.
</enforcement>