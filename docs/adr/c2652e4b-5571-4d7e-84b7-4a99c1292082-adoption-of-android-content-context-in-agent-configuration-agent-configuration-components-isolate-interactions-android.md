---
version: 2
name: c2652e4b-5571-4d7e-84b7-4a99c1292082-adoption-of-android-content-context-in-agent-configuration-agent-configuration-components-isolate-interactions-android
description: "Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Isolate Interactions Android"
metadata:
  actual:
    adr_id: c2652e4b-5571-4d7e-84b7-4a99c1292082
    status: accepted
    candidate_group_id: 06e58e7e-b441-4116-99b3-d976f277c2e2
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/agent/**
      - src/main/kotlin/com/codeassist/agenttrader/agent/**
---

# Adoption of android.content.Context in Agent Configuration: Agent Configuration Components Isolate Interactions Android

Status: proposed
Date: 2025-05-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/agent/**`
- `src/main/kotlin/com/codeassist/agenttrader/agent/**`

## Context

- Direct dependency on android.content.Context was introduced into agent configuration components to supply runtime platform access during agent initialization.
- Agent configuration requires access to environment-specific storage, resources, or application services provided through platform context interfaces.
- Static analysis observes android.content.Context referenced across agent configuration definitions in multiple module locations.

## Problem Statement

Coupling agent configuration modules directly to android.content.Context introduces platform runtime dependencies into domain configuration components, creating coupling between agent initialization logic and platform application lifecycles while hindering standalone testing and non-platform execution.

## Decision

1. SHOULD: Agent configuration components SHOULD isolate interactions with android.content.Context behind an abstraction boundary to prevent platform dependencies from leaking into domain business logic.

## Policy Block

- SHOULD Agent configuration components SHOULD isolate interactions with android.content.Context behind an abstraction boundary to prevent platform dependencies from leaking into domain business logic.

In scope:
- Agent configuration components and classes that require platform resource or service access.
- Modules defining agent initialization parameters that interface with platform context services.

Out of scope:
- Pure domain models and trading business logic components that operate independently of platform runtime services.
- Platform-agnostic utility functions and algorithmic components.

Exceptions:
- EXC-20-001: A component requires access during testing with synthetic or mock context implementations.

## Rationale

- Direct integration with android.content.Context provides agent configuration components with immediate access to platform-managed application storage, assets, and system facilities.
- Establishing explicit injection boundaries around android.content.Context prevents memory leaks caused by retaining transient lifecycle references across agent lifespans.
- Documenting this dependency enforces architectural separation between platform-dependent configuration wiring and platform-agnostic trading logic.

## Consequences

Positive:
- Enables agent configuration components to load platform-managed assets, preferences, and system resources consistently.
- Clarifies dependency ownership by mandating explicit passing of context instances rather than implicit ambient access.
- Facilitates controlled isolation of platform-specific runtime concerns from core agent algorithms.

Negative:
- Couples agent configuration components to platform runtime classes, preventing compilation and execution in pure non-platform environments.
- Increases testing setup complexity by requiring platform context mocks or runtime fixtures during component tests.

## Alternatives

- Abstract Configuration Provider Interface without android.content.Context Dependency (rejected)
  Rejected because: Introducing an intermediary abstraction provider layer across all configuration points adds indirection overhead before multi-platform support is required.
  When valid: When agent configuration must run interchangeably across multiple non-platform runtime targets.
- Direct android.content.Context Dependency in Agent Configuration (accepted)
  Rejected because: Not rejected; adopted as the baseline platform integration pattern.
  When valid: When operating within a platform environment where configuration relies directly on platform-provided runtime facilities.

## Risks

- Retaining an activity or transient context reference inside long-lived agent configuration causes platform memory leaks.
  Mitigation: Enforce the use of application-scoped context or ensure references are cleared upon agent deactivation.
  Owner: Engineering Team
- Direct coupling to platform classes limits execution of automated tests outside platform runtime environments.
  Mitigation: Encapsulate context-dependent configuration reads behind focused interfaces or mockable wrappers.
  Owner: Engineering Team

## Implementation Notes

- DISCOVERY POLICY (MANDATORY): This ADR omits all tool names, file names, commands, package managers, and version numbers. The consumer MUST derive them from the project repository.

LOCK-VERSION GROUNDING (MANDATORY) — before writing code that uses a versioned library, execute in order:
1. Find the dependency manifest in the repo. It declares ranges, not installed versions.
2. Identify the build tool from the manifest.
3. Inspect the repository lock or resolution artifact to determine the exact resolved version. This artifact is authoritative; build-tool output only verifies the active environment matches it.
4. Look up the official documentation, changelog, or public API reference for that exact version. Do not use training-data recall — fetch or search the public internet for version-specific docs.
5. Confirm every API, class, or function you will call exists in that exact version's documentation before using it.
6. For version-sensitive behavior, re-run steps 3-5 per dependency at point of use.
- Ensure context references supplied to agent configuration components utilize the application context whenever configuration lifecycle matches the application lifespan.
- Extract property values from platform resources at initialization time to avoid holding persistent context references inside configuration state.

## Continuation Context


Verify commands:
- Discover the project static analysis verification script from the build configuration and execute it to verify that no unauthorized context references or lifecycle violations are detected.
- Discover and run the project automated test suite to confirm that agent configuration components initialize properly with mock or platform context instances.

Accept when:
- All discovered static analysis checks report zero violations regarding context reference retention or unauthorized platform dependencies.
- All unit and integration tests covering agent configuration pass successfully with verified context resolution.

## Enforcement

- Verified by: Automated code inspection and lint rules in the continuous integration pipeline.
- Verified by: Peer code review verification of context scoping and lifecycle adherence.
- Violation handling: Pull requests introducing direct context retention in domain layers or failing lint checks are blocked from merging.
- Violation handling: Violations identified post-merge are tracked as technical debt issues requiring remediation.
- Exception process: Submit an architectural review request detailing the necessity of direct context binding and the associated lifecycle safeguards.