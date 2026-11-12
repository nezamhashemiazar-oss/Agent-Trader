---
version: 2
name: cross-cutting-consumers-discover-repository-lock-artif-a6f6
description: "Strategy Protocol and StrategyRegistry Contract: Consumers Discover Repository Lock Artifact Verify"
metadata:
  actual:
    adr_id: a6f60763-55f1-468f-b9a8-af76db7ac5bb
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

# Strategy Protocol and StrategyRegistry Contract: Consumers Discover Repository Lock Artifact Verify

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-STRAT-001** MUST: Consumers MUST discover the repository lock artifact and verify the exact resolved versions of all internal and external dependencies prior to implementation.

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