---
version: 2
name: cross-cutting-trading-engine-components-interacting-bi-7750
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Engine Components Interacting Bitunixrest Translate"
metadata:
  actual:
    adr_id: 7750c2e0-0a27-4225-8579-67c76a868d40
    status: accepted
    candidate_group_id: d88fa197-1133-4662-87ac-0060a1755fe6
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/trader/**
      - src/main/kotlin/com/codeassist/agenttrader/trader/**
---

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Engine Components Interacting Bitunixrest Translate

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-BITUNIX-001** MUST: Trading engine components interacting with BitunixRest MUST translate exchange-specific order and execution payloads into internal Position contracts before publishing state changes to dependent subsystems.

### Verify

```bash
# Discover and execute the repository compilation command
# Discover and execute the repository test suite
```

**Accept when:**
- The project build succeeds with all trading engine components referencing BitunixRest through the designated client module without circular dependencies.
- Automated test suites pass, verifying that Candle ingestion and Position generation conform to expected business contracts.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>