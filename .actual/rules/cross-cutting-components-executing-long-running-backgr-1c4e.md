---
version: 2
name: cross-cutting-components-executing-long-running-backgr-1c4e
description: "android.os.Handler Task Dispatch via post: Components Executing Long Running Background Tasks"
metadata:
  actual:
    adr_id: 1c4ee4e7-1117-4be9-a1d3-d0ad6c75af2f
    status: accepted
    candidate_group_id: 33caf30c-ee2f-4a08-9b3b-f52ddbe16344
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/**
      - src/main/kotlin/com/codeassist/agenttrader/**
---

# android.os.Handler Task Dispatch via post: Components Executing Long Running Background Tasks

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-HANDLER-001** SHOULD: Components executing long-running background tasks SHOULD decouple domain business logic from android.os.Handler message posting by introducing an abstract dispatcher boundary.
- **R-HANDLER-002** MUST: Verify that all instances of android.os.Handler explicitly bind to a well-defined android.os.Looper rather than relying on implicit ambient loopers.
- **R-HANDLER-003** MUST: Ensure that tasks posted via post do not perform blocking synchronous I/O operations on the main UI looper thread.

### Verify

```bash
# Discover and run the project's static analysis and linting verification scripts to detect unmanaged message posting.
# Discover and execute the project unit and integration test suite to validate thread marshaling and lifecycle cleanup.
```

**Accept when:**
- All unit and component tests pass without thread contention or unhandled looper exceptions.
- Static analysis checks confirm no memory leaks or uncleared posted callbacks in activity components.

<enforcement>
Claude Code MUST NOT skip or defer verification. Automated continuous integration build checks and peer code reviews verify adherence to thread boundary rules and lifecycle cleanup.
</enforcement>