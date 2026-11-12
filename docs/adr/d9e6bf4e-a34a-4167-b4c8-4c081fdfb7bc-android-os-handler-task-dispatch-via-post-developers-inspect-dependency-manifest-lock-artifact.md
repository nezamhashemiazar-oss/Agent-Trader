---
version: 2
name: d9e6bf4e-a34a-4167-b4c8-4c081fdfb7bc-android-os-handler-task-dispatch-via-post-developers-inspect-dependency-manifest-lock-artifact
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

Status: proposed
Date: 2025-05-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/**`
- `src/main/kotlin/com/codeassist/agenttrader/**`

## Context

- The codebase requires task scheduling and execution coordination between asynchronous execution contexts and thread message queues.
- Components extending androidx.activity.ComponentActivity receive UI events and manage lifecycle state while coordinating background operations.
- The detected pattern uses android.os.Handler and android.os.Looper to post execution units onto message queues via post.
- Static analysis identifies this pattern under service and API boundary definitions, representing an in-process thread execution boundary rather than a remote service endpoint.

## Problem Statement

When components coordinate asynchronous operations and UI state, execution must be marshaled across thread boundaries without blocking or corrupting thread-confined resources. Uncontrolled dispatch mechanisms create coupling between business logic and platform-specific message queues, requiring a defined boundary for posting tasks via android.os.Handler.

## Decision

1. MUST: Developers MUST inspect the dependency manifest and lock artifact in the project repository to identify and verify the exact resolved version of all core platform libraries prior to implementation.

## Policy Block

- MUST Developers MUST inspect the dependency manifest and lock artifact in the project repository to identify and verify the exact resolved version of all core platform libraries prior to implementation.

In scope:
- Activity components that coordinate task dispatch to message queues via android.os.Handler and android.os.Looper
- In-process execution boundaries handling UI thread marshaling

Out of scope:
- Out-of-process IPC and remote service boundaries
- Stateless utility functions with no thread affinity

## Rationale

- Using android.os.Handler with android.os.Looper provides deterministic message queue task posting via post for thread-confined operations.
- Restricting message posting to explicit handler boundaries prevents race conditions when updating state in components using androidx.activity.ComponentActivity.
- Direct usage of post establishes an in-process execution boundary, though it requires disciplined lifecycle management to avoid leaking runnable references.

## Consequences

Positive:
- Provides deterministic ordering and execution dispatch on designated looper thread queues.
- Ensures safe marshaling of asynchronous results back to target thread contexts.
- Utilizes native platform capabilities without adding external third-party scheduling abstractions.

Negative:
- Tight coupling between activity components and platform-specific message queue constructs.
- Risk of memory retention or lifecycle leaks if pending posted runnables hold references to destroyed activity contexts.
- Limited abstraction makes unit testing difficult without specialized mocking or shadow loopers.

## Alternatives

- Remote Service or IPC Interface (rejected)
  Rejected because: Inter-process communication introduces unnecessary serialization overhead and architectural complexity for strictly in-process activity thread dispatch.
  When valid: When tasks must execute in an isolated process or cross security boundaries.
- Direct Thread Execution without Looper Queues (rejected)
  Rejected because: Spawning raw unmanaged threads bypasses message queue synchronization and lacks thread affinity for UI component state updates.
  When valid: When running fire-and-forget CPU-bound tasks that never interact with thread-confined resources.

## Risks

- Retaining references to destroyed activity components inside unexecuted posted runnables leads to memory leaks.
  Mitigation: Ensure handlers clear pending callbacks or decouple task references during lifecycle destruction.
  Owner: Engineering Team
- Direct reliance on android.os.Handler in domain logic impedes isolated unit testing outside a looper environment.
  Mitigation: Encapsulate handler interactions behind an abstraction layer and verify behavior via project test suites.
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
- Verify that all instances of android.os.Handler explicitly bind to a well-defined android.os.Looper rather than relying on implicit ambient loopers.
- Ensure that tasks posted via post do not perform blocking synchronous I/O operations on the main UI looper thread.

## Continuation Context


Verify commands:
- Discover and run the project's static analysis and linting verification scripts to detect unmanaged message posting.
- Discover and execute the project unit and integration test suite to validate thread marshaling and lifecycle cleanup.

Accept when:
- All unit and component tests pass without thread contention or unhandled looper exceptions.
- Static analysis checks confirm no memory leaks or uncleared posted callbacks in activity components.

## Enforcement

- Verified by: Automated continuous integration build checks running repository test and lint suites.
- Verified by: Peer code review verifying adherence to thread boundary rules and lifecycle cleanup.
- Violation handling: Pull requests introducing unmanaged thread dispatch or unverified dependencies will be blocked until remediated.
- Exception process: Submit an architectural exception request detailing technical justification and obtain approval from the engineering architecture group.