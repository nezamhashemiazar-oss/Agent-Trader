---
version: 2
name: cross-cutting-developers-inspect-dependency-manifest-l-d9e6
description: "android.os.Handler Task Dispatch via post: Developers Inspect Dependency Manifest Lock Artifact"
metadata:
  actual:
    adr_id: d9e6bf4e-a34a-4167-b4c8-4c081fdfb7bc
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

# android.os.Handler Task Dispatch via post: Developers Inspect Dependency Manifest Lock Artifact

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for activity components coordinating task dispatch to message queues via android.os.Handler and android.os.Looper, and in-process execution boundaries handling UI thread marshaling.

### Rules

- **R-HANDLER-001** MUST: Developers MUST inspect the dependency manifest and lock artifact in the project repository to identify and verify the exact resolved version of all core platform libraries prior to implementation.
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
Claude Code MUST NOT skip or defer verification.
</enforcement>