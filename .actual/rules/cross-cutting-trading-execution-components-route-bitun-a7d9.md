---
version: 2
name: cross-cutting-trading-execution-components-route-bitun-a7d9
description: "Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Execution Components Route Bitunix Exchange"
metadata:
  actual:
    adr_id: a7d98e18-4d22-4057-97f2-38fcca9bd2b6
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

# Adoption of com.codeassist.agenttrader.bitunix.BitunixRest Client Module: Trading Execution Components Route Bitunix Exchange

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/trader/**`, `src/main/kotlin/com/codeassist/agenttrader/trader/**`.

These rules are ALWAYS ACTIVE for trading engine execution components responsible for market data ingestion, order management, and exchange interactions with Bitunix.

### Rules

- **R-BITUNIX-001** MUST: Trading execution components MUST route all Bitunix exchange REST communications through com.codeassist.agenttrader.bitunix.BitunixRest client instances rather than issuing raw HTTP requests directly.

### Verify

```bash
# Discover and execute the repository compilation command
# (e.g., mvn compile, gradle build, npm run build, etc., based on project manifest)
# Discover and execute the repository test suite
```

**Accept when:**
- The project build succeeds with all trading engine components referencing BitunixRest through the designated client module without circular dependencies.
- Automated test suites pass, verifying that Candle ingestion and Position generation conform to expected business contracts.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verification is mandatory via automated continuous integration checks and peer code reviews.
</enforcement>