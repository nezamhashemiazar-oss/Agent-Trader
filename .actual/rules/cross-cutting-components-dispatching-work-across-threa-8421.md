---
version: 2
name: cross-cutting-components-dispatching-work-across-threa-8421
description: "android.os.Handler Task Dispatch via post: Components Dispatching Work Across Thread Boundaries"
metadata:
  actual:
    adr_id: 84213d0f-0a64-4e5f-9ba0-c89df41b4831
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

# android.os.Handler Task Dispatch via post: Components Dispatching Work Across Thread Boundaries

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all Activity components coordinating task dispatch to message queues via android.os.Handler and android.os.Looper, and in-process execution boundaries handling UI thread marshaling.

### Rules

- **R-HANDLER-001** MUST: Components dispatching work across thread boundaries to a looper queue MUST route operations through android.os.Handler using post.
- **R-HANDLER-002** MUST: All instances of android.os.Handler MUST explicitly bind to a well-defined android.os.Looper rather than relying on implicit ambient loopers.
- **R-HANDLER-003** MUST: Tasks posted via post MUST NOT perform blocking synchronous I/O operations on the main UI looper thread.

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