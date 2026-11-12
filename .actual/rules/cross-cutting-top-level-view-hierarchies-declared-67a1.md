---
version: 2
name: cross-cutting-top-level-view-hierarchies-declared-67a1
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Top Level View Hierarchies Declared Within"
metadata:
  actual:
    adr_id: 67a1d291-461c-4f0d-86d3-9d71778002ff
    status: accepted
    candidate_group_id: 57f40fa6-394d-4ed3-a822-c03774c8c7fc
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/**
      - src/main/kotlin/com/codeassist/agenttrader/**
---

# Adoption of Composable UI Framework via ComponentActivity and setContent: Top Level View Hierarchies Declared Within

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all application host activities managing lifecycle events, rendering visual user interfaces, and modules integrating with ComponentActivity and declaring Composable view hierarchies via setContent.

### Rules

- **R-UI-001** MUST: Top-level view hierarchies declared within setContent MUST delegate visual presentation to Composable annotated functions.

### Verify

```bash
bash -c 'find . -maxdepth 3 -type f -name "*lock*" -o -name "*manifest*" -o -name "*build*" -o -name "*spec*"'
bash -c 'test_runner=$(find . -maxdepth 2 -type f -perm -111 -not -path "*/.*" | head -n 1); if [ -n "$test_runner" ]; then echo "Discovered execution runner: $test_runner"; else echo "Locate repository test or verification runner from project manifest"; fi'
```

**Accept when:**
- All application host activities implement user interface rendering via ComponentActivity and setContent using Composable functions
- Static code analysis confirms no imperative view tree inflation is present in activities governed by this decision
- Repository verification and compilation workflows execute cleanly against resolved dependency versions discovered from lock artifacts

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>