---
version: 2
name: cross-cutting-prior-adopting-compiling-against-applica-6f66
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Prior Adopting Compiling Against Application Framework"
metadata:
  actual:
    adr_id: 6f66c078-aec0-4c32-9ff6-bb27e351e215
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

# Adoption of Composable UI Framework via ComponentActivity and setContent: Prior Adopting Compiling Against Application Framework

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all application host activities managing lifecycle events, rendering visual user interfaces, and modules integrating with ComponentActivity and declaring Composable view hierarchies via setContent.

### Rules

- **R-UI-001** MUST: Prior to adopting or compiling against application framework dependencies, the consumer MUST inspect the repository dependency manifest and lock artifact to determine the authoritative resolved version.
- **R-UI-002** MUST: Priorly inspect repository dependency manifest and lock artifact before writing code that uses a versioned library.
- **R-UI-003** MUST: Initialize composition root structures within lifecycle creation callbacks using Bundle arguments for state recovery.
- **R-UI-004** MUST: Post background operations UI updates via Handler instances configured with the application main Looper.

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