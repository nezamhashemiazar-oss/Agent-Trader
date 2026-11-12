---
version: 2
name: cross-cutting-thread-dispatch-asynchronous-message-han-c003
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Thread Dispatch Asynchronous Message Handling Coordinate"
metadata:
  actual:
    adr_id: c003e684-d94b-421b-af25-3dd33cb0c7d0
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

# Adoption of Composable UI Framework via ComponentActivity and setContent: Thread Dispatch Asynchronous Message Handling Coordinate

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-UI-001** SHOULD: Thread dispatch and asynchronous UI message handling SHOULD coordinate execution through Looper and Handler attached to the main application loop.
- **R-UI-002** MANDATORY: The consumer MUST derive all tool names, file names, commands, package managers, and version numbers from the project repository.
- **R-UI-003** MANDATORY: Before writing code that uses a versioned library, the developer MUST execute in order: (1) Find the dependency manifest, (2) Identify the build tool, (3) Inspect the repository lock or resolution artifact to determine the exact resolved version, (4) Look up official documentation for that exact version, (5) Confirm every API exists in that exact version, (6) Re-run steps 3-5 per dependency at point of use.
- **R-UI-004** MANDATORY: Host activities must initialize composition root structures within lifecycle creation callbacks using Bundle arguments for state recovery.
- **R-UI-005** MANDATORY: Background operations must post UI updates via Handler instances configured with the application main Looper.

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