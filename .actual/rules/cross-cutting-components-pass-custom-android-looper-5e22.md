---
version: 2
name: cross-cutting-components-pass-custom-android-looper-5e22
description: "android.os.Handler Task Dispatch via post: Components Pass Custom Android Looper Instances"
metadata:
  actual:
    adr_id: 5e22c649-1f79-4165-b823-ab41edcd59d3
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

# android.os.Handler Task Dispatch via post: Components Pass Custom Android Looper Instances

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all Activity components that coordinate task dispatch to message queues via android.os.Handler and android.os.Looper, and in-process execution boundaries handling UI thread marshaling.

### Rules

- **R-HANDLER-001** MAY: Components MAY pass custom android.os.Looper instances to android.os.Handler when scheduling tasks to dedicated non-main execution threads.
- **R-HANDLER-002** MANDATORY: Verify that all instances of android.os.Handler explicitly bind to a well-defined android.os.Looper rather than relying on implicit ambient loopers.
- **R-HANDLER-003** MANDATORY: Ensure that tasks posted via post do not perform blocking synchronous I/O operations on the main UI looper thread.
- **R-HANDLER-004** MANDATORY (LOCK-VERSION GROUNDING): Before writing code that uses a versioned library, execute in order: (1) Find the dependency manifest in the repo. (2) Identify the build tool from the manifest. (3) Inspect the repository lock or resolution artifact to determine the exact resolved version. (4) Look up official documentation/changelog for that exact version. (5) Confirm every API, class, or function used exists in that exact version's docs. (6) Re-run steps 3-5 per dependency at point of use for version-sensitive behavior.

### Verify

```bash
# Discover and run the project's static analysis and linting verification scripts to detect unmanaged message posting.
# Discover and execute the project unit and integration test suite to validate thread marshaling and lifecycle cleanup.
```

**Accept when:**
- All unit and component tests pass without thread contention or unhandled looper exceptions.
- Static analysis checks confirm no memory leaks or uncleared posted callbacks in activity components.

<enforcement>
Claude Code MUST NOT skip or defer verification. Verified by automated continuous integration build checks and peer code review. Violation handling: Pull requests introducing unmanaged thread dispatch or unverified dependencies will be blocked until remediated.
</enforcement>