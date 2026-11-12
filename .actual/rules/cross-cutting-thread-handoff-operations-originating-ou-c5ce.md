---
version: 2
name: cross-cutting-thread-handoff-operations-originating-ou-c5ce
description: "Adoption of androidx.activity.compose.setContent for Activity UI Composition: Thread Handoff Operations Originating Outside Composition"
metadata:
  actual:
    adr_id: c5ce3125-1c47-4783-ae3d-4cb39e9dde64
    status: accepted
    candidate_group_id: d88e93dd-82c9-439b-b90b-c533eaa78b3b
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/**
      - src/main/kotlin/com/codeassist/agenttrader/**
---

# Adoption of androidx.activity.compose.setContent for Activity UI Composition: Thread Handoff Operations Originating Outside Composition

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-THRD-001** SHOULD: Thread handoff operations originating from outside the composition runtime to the main looper SHOULD use android.os.Handler configured with android.os.Looper or native composition coroutine contexts rather than manual thread synchronization.

### Verify

```bash
# Run the project static analysis and linting verification suites to confirm UI initialization conforms to androidx.activity.compose.setContent conventions.
# Execute compilation and architecture verification suites to verify no legacy view inflation APIs are referenced within activity classes.
```

**Accept when:**
- All UI activity classes inherit from androidx.activity.ComponentActivity and initialize visual hierarchy through androidx.activity.compose.setContent without compiler errors or lint warnings.
- Project verification suites pass with zero architecture rule violations.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>