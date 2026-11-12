---
version: 2
name: 86b8b0b0-cb25-43e4-919e-39dfff6899e1-adoption-of-androidx-activity-compose-setcontent-for-activity-ui-composition-user-interface-trees-supplied-androidx-activity
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

Status: proposed
Date: 2025-05-14
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/**`
- `src/main/kotlin/com/codeassist/agenttrader/**`

## Context

- Activity components require a standardized integration mechanism to host declarative user interfaces while participating in platform lifecycle dispatch.
- The codebase establishes UI presentation via androidx.activity.compose.setContent hosted inside androidx.activity.ComponentActivity instead of conventional XML layout inflation.
- Core runtime coordination interacts with platform lifecycle bundles and thread loopers via android.os.Bundle, android.os.Handler, and android.os.Looper.

## Problem Statement

Unifying activity entry points across the codebase requires an authoritative pattern for bootstrapping user interface trees. Without a defined architectural standard for activity layout initialization, components risk mixing imperative view inflation with declarative composition trees, causing fractured state management, duplicate lifecycle listeners, and inconsistent main-thread execution dispatch.

## Decision

1. MUST: All user interface trees supplied to androidx.activity.compose.setContent MUST be constructed from root functions annotated with Composable.

## Policy Block

- MUST All user interface trees supplied to androidx.activity.compose.setContent MUST be constructed from root functions annotated with Composable.

In scope:
- Activity classes responsible for presenting user interfaces
- Entry-point UI containers bound to the platform lifecycle

Out of scope:
- Background services, broadcast receivers, and non-UI worker components
- Headless platform components lacking visual display requirements

## Rationale

- Using androidx.activity.compose.setContent on androidx.activity.ComponentActivity establishes a single declarative UI root, eliminating view hierarchy bridging overhead.
- Grounded evidence shows concurrent presence of Composable root nodes alongside android.os.Handler and android.os.Looper for thread dispatch, establishing an integrated activity architecture.
- Enforcing a uniform activity hosting pattern prevents architectural drift between imperative view bindings and modern declarative composition.

## Consequences

Positive:
- Provides a single, standardized pattern for mounting declarative UI trees directly from activity entry points.
- Eliminates boilerplate associated with traditional XML layout inflation and view binding synthesis.
- Aligns activity lifecycle management directly with Compose runtime composition and disposal cycles.

Negative:
- Tightly couples activity definitions to the androidx.activity.compose integration library.
- Requires background thread dispatchers using android.os.Handler or android.os.Looper to carefully synchronize with composition recomposition phases.

## Alternatives

- Imperative XML layout inflation with platform view binding (rejected)
  Rejected because: Increases boilerplate, duplicates UI state across platform views and composables, and complicates reactive state propagation.
  When valid: Valid only in legacy activity modules that cannot adopt declarative composition toolchains.
- Hosting composables via ComposeView inside XML layouts (rejected)
  Rejected because: Introduces unnecessary layout hierarchy indirection when the entire activity UI is built with Composable trees.
  When valid: Valid when incrementally migrating legacy hybrid screens containing non-composable legacy platform views.

## Risks

- Main thread stalling when dispatching asynchronous tasks through android.os.Handler while recomposition is active
  Mitigation: Offload heavy computation to background dispatchers and restrict main looper handler invocations to lightweight state updates.
  Owner: engineering team
- State loss during configuration changes if android.os.Bundle restoration is not coordinated with Compose state saving
  Mitigation: Ensure state preservation strategies align between activity bundle callbacks and composition rememberSaveable mechanisms.
  Owner: engineering team

## Implementation Notes

- DISCOVERY POLICY (MANDATORY): This ADR omits all tool names, file names, commands, package managers, and version numbers. The consumer MUST derive them from the project repository.

LOCK-VERSION GROUNDING (MANDATORY) — before writing code that uses a versioned library, execute in order:
1. Find the dependency manifest in the repo. It declares ranges, not installed versions.
2. Identify the build tool from the manifest.
3. Inspect the repository lock or resolution artifact to determine the exact resolved version. This artifact is authoritative; build-tool output only verifies the active environment matches it.
4. Look up the official documentation, changelog, or public API reference for that exact version. Do not use training-data recall — fetch or search the public internet for version-specific docs.
5. Confirm every API, class, or function you will call exists in that exact version's documentation before using it.
6. For version-sensitive behavior, re-run steps 3-5 per dependency at point of use.
- Extend androidx.activity.ComponentActivity and invoke androidx.activity.compose.setContent inside the lifecycle creation callback after handling saved instance state from android.os.Bundle.
- Encapsulate the entire activity visual tree in a top-level Composable function to ensure seamless recomposition scoping and theme propagation.

## Continuation Context


Verify commands:
- Run the project static analysis and linting verification suites to confirm UI initialization conforms to androidx.activity.compose.setContent conventions.
- Execute compilation and architecture verification suites to verify no legacy view inflation APIs are referenced within activity classes.

Accept when:
- All UI activity classes inherit from androidx.activity.ComponentActivity and initialize visual hierarchy through androidx.activity.compose.setContent without compiler errors or lint warnings.
- Project verification suites pass with zero architecture rule violations.

## Enforcement

- Verified by: Automated static analysis checks executed during pull request CI validation
- Verified by: Peer code review confirming proper usage of androidx.activity.compose.setContent and absence of legacy layout inflation
- Violation handling: Pull requests introducing imperative view inflation or bypassing androidx.activity.compose.setContent in new UI activities must be blocked from merging until refactored.
- Exception process: Exceptions for legacy hybrid activities require written approval from the architecture review board with an accompanying migration issue.