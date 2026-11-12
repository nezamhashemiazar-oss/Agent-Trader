---
version: 2
name: cross-cutting-host-activities-decompose-complex-layout-edc0
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Host Activities Decompose Complex Layouts Modular"
metadata:
  actual:
    adr_id: edc0c20a-9f7e-41b4-9a21-95fec9b40bac
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

# Adoption of Composable UI Framework via ComponentActivity and setContent: Host Activities Decompose Complex Layouts Modular

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all application host activities managing lifecycle events and rendering visual user interfaces, and modules integrating with ComponentActivity and declaring Composable view hierarchies via setContent.

### Rules

- **R-ACT-001** MAY: Host activities MAY decompose complex UI layouts into modular Composable functions to isolate presentation responsibilities.
- **R-ACT-002** MANDATORY: Host activities must initialize composition root structures within lifecycle creation callbacks using Bundle arguments for state recovery.
- **R-ACT-003** MANDATORY: Background operations must post UI updates via Handler instances configured with the application main Looper.
- **R-ACT-004** MANDATORY: Lock-Version Grounding — before writing code that uses a versioned library, execute in order: find dependency manifest, identify build tool, inspect repository lock/resolution artifact, look up official documentation for exact version, confirm every API exists in that exact version's documentation, and re-run per dependency at point of use.

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