---
version: 2
name: cross-cutting-facing-components-utilizing-androidx-act-16a8
description: "android.os.Handler Task Dispatch via post: Facing Components Utilizing Androidx Activity Componentactivity"
metadata:
  actual:
    adr_id: 16a81785-67bc-4c3e-8b55-49bba2dbf614
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

# android.os.Handler Task Dispatch via post: Facing Components Utilizing Androidx Activity Componentactivity

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-HANDLER-001** SHOULD: UI-facing components utilizing androidx.activity.ComponentActivity with androidx.activity.compose.setContent SHOULD ensure posted callbacks clean up pending messages when the component is destroyed.

### Verify

```bash
# Discover and run the project's static analysis and linting verification scripts to detect unmanaged message posting.
# Discover and execute the project unit and integration test suite to validate thread marshaling and lifecycle cleanup.
```

**Accept when:**
- All unit and component tests pass without thread contention or unhandled looper exceptions.
- Static analysis checks confirm no memory leaks or uncleared posted callbacks in activity components.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>