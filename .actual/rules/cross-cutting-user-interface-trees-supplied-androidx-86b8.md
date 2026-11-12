---
version: 2
name: cross-cutting-user-interface-trees-supplied-androidx-86b8
description: "Adoption of androidx.activity.compose.setContent for Activity UI Composition: User Interface Trees Supplied Androidx Activity"
metadata:
  actual:
    adr_id: 86b8b0b0-cb25-43e4-919e-39dfff6899e1
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

# Adoption of androidx.activity.compose.setContent for Activity UI Composition: User Interface Trees Supplied Androidx Activity

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ACT-001** MUST: All user interface trees supplied to androidx.activity.compose.setContent MUST be constructed from root functions annotated with Composable.

### Verify

```bash
# Run the project static analysis and linting verification suites to confirm UI initialization conforms to androidx.activity.compose.setContent conventions.
# Execute compilation and architecture verification suites to verify no legacy view inflation APIs are referenced within activity classes.
```

**Accept when:**
- All UI activity classes inherit from androidx.activity.ComponentActivity and initialize visual hierarchy through androidx.activity.compose.setContent without compiler errors or lint warnings.
- Project verification suites pass with zero architecture rule violations.

<enforcement>
Claude Code MUST NOT skip or defer verification. All UI activity classes must initialize visual hierarchy through androidx.activity.compose.setContent with no legacy view inflation.
</enforcement>