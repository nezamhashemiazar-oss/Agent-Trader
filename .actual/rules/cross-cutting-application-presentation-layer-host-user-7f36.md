---
version: 2
name: cross-cutting-application-presentation-layer-host-user-7f36
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Application Presentation Layer Host User Interface"
metadata:
  actual:
    adr_id: 7f36171b-44b5-40ba-9f2a-3b1d1a12c456
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

# Adoption of Composable UI Framework via ComponentActivity and setContent: Application Presentation Layer Host User Interface

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for application host activities managing lifecycle events and rendering visual user interfaces, and modules integrating with ComponentActivity and declaring Composable view hierarchies via setContent.

### Rules

- **R-UI-001** MUST: The application presentation layer MUST host user interface hierarchies by extending ComponentActivity and invoking setContent with Composable functions as the primary visual composition mechanism.

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