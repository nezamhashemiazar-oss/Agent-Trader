---
version: 2
name: 6f66c078-aec0-4c32-9ff6-bb27e351e215-adoption-of-composable-ui-framework-via-componentactivity-and-setcontent-prior-adopting-compiling-against-application-framework
description: "Adoption of Composable UI Framework via ComponentActivity and setContent: Prior Adopting Compiling Against Application Framework"
metadata:
  actual:
    adr_id: 6f66c078-aec0-4c32-9ff6-bb27e351e215
    status: accepted
    candidate_group_id: 57f40fa6-394d-4ed3-a822-c03774c8c7fc
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/**
      - src/main/kotlin/com/codeassist/agenttrader/**
---

# Adoption of Composable UI Framework via ComponentActivity and setContent: Prior Adopting Compiling Against Application Framework

Status: proposed
Date: 2025-02-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/**`
- `src/main/kotlin/com/codeassist/agenttrader/**`

## Context

- The application requires a consistent framework for declaring user interface hierarchies and binding presentation logic to host activity lifecycles.
- Multiple components across the application source tree establish entry point activities extending ComponentActivity and rendering declarative Composable views.
- Asynchronous task coordination and state restoration require standardized integration with Handler, Looper, and Bundle abstractions.

## Problem Statement

Host activities require a standardized, maintainable pattern for declaring user interface hierarchies and synchronizing state across lifecycle transitions without relying on fragmented imperative layout inflation or inconsistent message dispatch mechanisms.

## Decision

1. MUST: Prior to adopting or compiling against application framework dependencies, the consumer MUST inspect the repository dependency manifest and lock artifact to determine the authoritative resolved version.

## Policy Block

- MUST Prior to adopting or compiling against application framework dependencies, the consumer MUST inspect the repository dependency manifest and lock artifact to determine the authoritative resolved version.

In scope:
- Application host activities managing lifecycle events and rendering visual user interfaces
- Modules integrating with ComponentActivity and declaring Composable view hierarchies via setContent

Out of scope:
- Background workers, headless services, and non-visual components that do not render UI elements
- Domain logic and data repository layers operating independently of activity lifecycles and presentation frameworks

## Rationale

- Adopting ComponentActivity with setContent establishes a clean contract where declarative Composable functions manage view structures and reactive state.
- Standardizing asynchronous message coordination via Handler and Looper ensures deterministic dispatch back to the primary thread loop.
- Evidence across application entry points demonstrates consistent adoption of Composable UI binding over legacy layout inflation.

## Consequences

Positive:
- Eliminates boilerplate imperative layout inflation and view binding synchronization
- Establishes unidirectional data flow and reactive interface composition anchored in ComponentActivity
- Provides deterministic lifecycle-aware UI hierarchy initialization within setContent

Negative:
- Directly couples presentation rendering entry points to host activity lifecycle boundaries
- Requires strict discipline regarding thread confinement when dispatching background events to the main loop via Handler and Looper
- Mandates team familiarity with declarative reactivity and state hoisting conventions rather than imperative view manipulation

## Alternatives

- Imperative View Tree Layout Inflation (rejected)
  Rejected because: Requires bidirectional state synchronization between imperative view objects and domain models, increasing lifecycle complexity and mutation bugs
  When valid: When integrating legacy view components that cannot be wrapped within declarative host containers
- Raw Background Threading Without Main Looper Dispatch (rejected)
  Rejected because: Direct background thread execution risks concurrent modification of UI state and violates thread affinity constraints managed by Looper and Handler
  When valid: When computing intensive background calculations isolated entirely from user interface state updates

## Risks

- Tight coupling between activity lifecycle events and UI state composition can impede modular testing
  Mitigation: Enforce strict separation between host activity lifecycle logic and pure Composable presentation nodes
  Owner: engineering team
- Improper message dispatch via Handler without proper Looper coordination may cause memory retention or threading violations
  Mitigation: Enforce proper Looper message queue dispatch and lifecycle cancellation checks before updating state from background handlers
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
- Host activities must initialize composition root structures within lifecycle creation callbacks using Bundle arguments for state recovery.
- Background operations must post UI updates via Handler instances configured with the application main Looper.

## Continuation Context


Verify commands:
- bash -c 'find . -maxdepth 3 -type f -name "*lock*" -o -name "*manifest*" -o -name "*build*" -o -name "*spec*"'
- bash -c 'test_runner=$(find . -maxdepth 2 -type f -perm -111 -not -path "*/.*" | head -n 1); if [ -n "$test_runner" ]; then echo "Discovered execution runner: $test_runner"; else echo "Locate repository test or verification runner from project manifest"; fi'

Accept when:
- All application host activities implement user interface rendering via ComponentActivity and setContent using Composable functions
- Static code analysis confirms no imperative view tree inflation is present in activities governed by this decision
- Repository verification and compilation workflows execute cleanly against resolved dependency versions discovered from lock artifacts

## Enforcement

- Verified by: Automated static analysis scanning activity definitions for ComponentActivity inheritance and setContent usage
- Verified by: Continuous integration build pipelines validating lock-file alignment and dependency resolution integrity
- Verified by: Peer code reviews verifying adherence to declarative Composable conventions and thread dispatch isolation
- Violation handling: Pull requests introducing imperative view layouts in governed activities will fail automated static checks
- Violation handling: Direct UI state mutations bypassing Composable reactivity or main loop message queues trigger architectural review flags
- Violation handling: Non-compliant code must be refactored to use declarative composition before merging to main branch
- Exception process: Submit an architectural variance request detailing technical incompatibilities with declarative UI hosting
- Exception process: Require sign-off from the technical lead prior to introducing non-conforming view inflation mechanisms
- Exception process: Document approved exceptions with a sunset plan for eventual migration to Composable view structures