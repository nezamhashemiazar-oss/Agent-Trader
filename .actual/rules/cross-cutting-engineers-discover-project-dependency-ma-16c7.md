---
version: 2
name: cross-cutting-engineers-discover-project-dependency-ma-16c7
description: "Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Engineers Discover Project Dependency Manifest Lock"
metadata:
  actual:
    adr_id: 16c73bb0-44db-4b78-a09f-e262397e4f49
    status: accepted
    candidate_group_id: 3d2f5ae7-e34a-46d3-8cf8-ec6225495130
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Adoption of com.codeassist.agenttrader.bitunix.Candle as Canonical Market Data Representation: Engineers Discover Project Dependency Manifest Lock

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-TRAD-001** MUST: Engineers MUST discover the project dependency manifest and lock artifact to resolve and verify exact dependency versions before integrating or modifying module dependencies.

### Verify

```bash
# Discover and execute the project compilation and static analysis suite to verify type contracts and dependency imports.
# Discover and execute the project automated test suite covering indicator calculations, strategy voting, and execution engine workflows.
```

**Accept when:**
- All trader components compile successfully without unresolved references or type mismatches against com.codeassist.agenttrader.bitunix.Candle.
- Automated test suites pass, confirming strategy voting, indicator computation, and signal generation correctly process sequences of com.codeassist.agenttrader.bitunix.Candle.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>