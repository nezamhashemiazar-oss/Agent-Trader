---
version: 2
name: cross-cutting-activity-classes-initializing-androidx-c-4d10
description: "Adoption of androidx.activity.compose.setContent for Activity UI Composition: Activity Classes Initializing Androidx Compose Setcontent"
metadata:
  actual:
    adr_id: 4d10679f-fd3d-42ad-840d-c67fba2d54c3
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

# Adoption of androidx.activity.compose.setContent for Activity UI Composition: Activity Classes Initializing Androidx Compose Setcontent

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ACT-001** MUST_NOT: Activity classes initializing UI with androidx.activity.compose.setContent MUST_NOT invoke imperative view hierarchy inflation or layout resource binding.

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