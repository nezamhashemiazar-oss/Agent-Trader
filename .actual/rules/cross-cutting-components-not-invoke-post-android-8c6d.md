---
version: 2
name: cross-cutting-components-not-invoke-post-android-8c6d
description: "android.os.Handler Task Dispatch via post: Components Not Invoke Post Android Handler"
metadata:
  actual:
    adr_id: 8c6da759-2048-44e4-9495-62e7072da62f
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

# android.os.Handler Task Dispatch via post: Components Not Invoke Post Android Handler

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all Activity components coordinating task dispatch to message queues via android.os.Handler and android.os.Looper, and in-process execution boundaries handling UI thread marshaling.

### Rules

- **R-HANDLER-001** MUST_NOT: Components MUST_NOT invoke post on an android.os.Handler instance whose associated android.os.Looper lifecycle has terminated or become invalid.

### Verify

```bash
# Discover and run the project's static analysis and linting verification scripts to detect unmanaged message posting.
# Discover and execute the project unit and integration test suite to validate thread marshaling and lifecycle cleanup.
```

**Accept when:**
- All unit and component tests pass without thread contention or unhandled looper exceptions.
- Static analysis checks confirm no memory leaks or uncleared posted callbacks in activity components.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verification is mandatory via automated continuous integration build checks and peer code review.
</enforcement>