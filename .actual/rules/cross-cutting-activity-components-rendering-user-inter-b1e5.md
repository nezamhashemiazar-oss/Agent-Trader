---
version: 2
name: cross-cutting-activity-components-rendering-user-inter-b1e5
description: "Adoption of androidx.activity.compose.setContent for Activity UI Composition: Activity Components Rendering User Interfaces Inherit"
metadata:
  actual:
    adr_id: b1e50dc6-96e9-4c52-971f-c89fbb70f098
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

# Adoption of androidx.activity.compose.setContent for Activity UI Composition: Activity Components Rendering User Interfaces Inherit

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ACT-001** MUST: Activity components rendering user interfaces MUST inherit from androidx.activity.ComponentActivity and initialize their view hierarchies exclusively using androidx.activity.compose.setContent.

### Verify

```bash
# Run the project static analysis and linting verification suites
./gradlew lint test
```

**Accept when:**
- All UI activity classes inherit from androidx.activity.ComponentActivity and initialize visual hierarchy through androidx.activity.compose.setContent without compiler errors or lint warnings.
- Project verification suites pass with zero architecture rule violations.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>