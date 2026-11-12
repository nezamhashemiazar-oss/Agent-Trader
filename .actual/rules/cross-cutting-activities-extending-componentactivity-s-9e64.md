---
version: 2
name: cross-cutting-activities-extending-componentactivity-s-9e64
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Activities Extending Componentactivity Setcontent Not Construct"
metadata:
  actual:
    adr_id: 9e64471e-6dd8-47d8-9e44-4ecd4294d8ca
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

# Adoption of Composable UI Framework via ComponentActivity and setContent: Activities Extending Componentactivity Setcontent Not Construct

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ACT-001** MUST_NOT: Activities extending ComponentActivity with setContent MUST NOT construct imperative view hierarchies through manual view inflation mechanisms.

### Verify

```bash
find . -maxdepth 3 -type f -name "*lock*" -o -name "*manifest*" -o -name "*build*" -o -name "*spec*"
test_runner=$(find . -maxdepth 2 -type f -perm -111 -not -path "*/.*" | head -n 1); if [ -n "$test_runner" ]; then echo "Discovered execution runner: $test_runner"; else echo "Locate repository test or verification runner from project manifest"; fi
```

**Accept when:**
- All application host activities implement user interface rendering via ComponentActivity and setContent using Composable functions
- Static code analysis confirms no imperative view tree inflation is present in activities governed by this decision
- Repository verification and compilation workflows execute cleanly against resolved dependency versions discovered from lock artifacts

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>