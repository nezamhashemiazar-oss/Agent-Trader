---
version: 2
name: cross-cutting-prior-introducing-updating-calls-against-af0c
description: "Adoption of androidx.activity.compose.setContent for Activity UI Composition: Prior Introducing Updating Calls Against Androidx"
metadata:
  actual:
    adr_id: af0c85ef-9e7c-492e-b5db-3905f7aee92e
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

# Adoption of androidx.activity.compose.setContent for Activity UI Composition: Prior Introducing Updating Calls Against Androidx

Scope: applies to `app/src/main/kotlin/com/codeassist/agenttrader/**`, `src/main/kotlin/com/codeassist/agenttrader/**`.

These rules are ALWAYS ACTIVE for all files matching the configured scope.

### Rules

- **R-ACT-001** MUST: Prior to introducing or updating calls against androidx.activity.compose.setContent, engineers MUST inspect the project dependency lock artifact to verify the exact resolved version against public reference documentation.
- **R-ACT-002** MUST: Extend androidx.activity.ComponentActivity and invoke androidx.activity.compose.setContent inside the lifecycle creation callback after handling saved instance state from android.os.Bundle.
- **R-ACT-003** MUST: Encapsulate the entire activity visual tree in a top-level Composable function to ensure seamless recomposition scoping and theme propagation.

### Verify

```bash
# Run the project static analysis and linting verification suites to confirm UI initialization conforms to androidx.activity.compose.setContent conventions.
./gradlew lint
# Execute compilation and architecture verification suites to verify no legacy view inflation APIs are referenced within activity classes.
./gradlew compileDebugSources
```

**Accept when:**
- All UI activity classes inherit from androidx.activity.ComponentActivity and initialize visual hierarchy through androidx.activity.compose.setContent without compiler errors or lint warnings.
- Project verification suites pass with zero architecture rule violations.

<enforcement>
Claude Code MUST NOT skip or defer verification.
</enforcement>