---
version: 2
name: 8abd2419-0eaa-4a41-bcbe-14edbe9bb033-org-json-adoption-for-network-payload-serialization-and-parsing-consumers-verify-repository-lock-resolution-artifact
description: "org.json Adoption for Network Payload Serialization and Parsing: Consumers Verify Repository Lock Resolution Artifact"
metadata:
  actual:
    adr_id: 8abd2419-0eaa-4a41-bcbe-14edbe9bb033
    status: accepted
    candidate_group_id: be6ce5a6-c433-4c64-b82a-ff7f8882d14a
    evidence:
      source: deep
  scope:
    root: .
    paths:
      - app/src/main/kotlin/com/codeassist/agenttrader/**
      - src/main/kotlin/com/codeassist/agenttrader/**
---

# org.json Adoption for Network Payload Serialization and Parsing: Consumers Verify Repository Lock Resolution Artifact

Status: proposed
Date: 2025-05-18
Deciders: Detection Pipeline (automated)

## Scope Paths

Root: `.` (whole repository)

Applies to:
- `app/src/main/kotlin/com/codeassist/agenttrader/**`
- `src/main/kotlin/com/codeassist/agenttrader/**`

## Context

- Network client components communicating with external model endpoints and cryptocurrency exchange REST services require structured payload serialization and response parsing.
- The codebase standardizes on org.json structures across HTTP client integrations to handle dynamic document trees.
- Direct JSON object manipulation provides fine-grained control over dynamic payloads without requiring heavy reflection-based schema mappings.

## Problem Statement

Multiple network client components require a standardized, lightweight mechanism for constructing and parsing JSON payloads over HTTP connections without introducing divergent serialization frameworks or unnecessary runtime overhead.

## Decision

1. MUST: Consumers MUST verify the repository lock or resolution artifact to determine the exact resolved version of org.json before implementation and confirm that all invoked APIs exist in that version.

## Policy Block

- MUST Consumers MUST verify the repository lock or resolution artifact to determine the exact resolved version of org.json before implementation and confirm that all invoked APIs exist in that version.

In scope:
- Network integration components and HTTP clients performing JSON payload construction and parsing
- External service adapters communicating with REST endpoints

Out of scope:
- Internal in-memory data structures not transmitted over network boundaries
- Non-JSON binary serialization protocols

## Rationale

- Adopting org.json across network client integrations provides a unified, lightweight approach to constructing and parsing JSON documents.
- Using JSONObject and JSONArray avoids heavy reflection-based mapping dependencies, maintaining a minimal footprint and predictable performance in execution environments.
- Standardizing on a single JSON processing library prevents conflicting serialization abstractions across separate integration modules.

## Consequences

Positive:
- Establishes consistent data serialization and deserialization patterns across external REST integration clients.
- Eliminates reliance on complex reflection configurations or code generation tools for payload handling.
- Maintains a minimal runtime footprint and provides straightforward object tree manipulation.

Negative:
- Manual field extraction and mapping between JSONObject instances and domain structures increases boilerplate code compared to declarative data-binding libraries.
- Lack of compile-time schema validation increases reliance on runtime key lookups and type conversions.

## Alternatives

- Adopt reflection-based JSON data binding frameworks (rejected)
  Rejected because: Introduces substantial runtime dependency overhead and complex annotation configurations for REST payloads
  When valid: When handling massive, deeply nested schema models with strict automated mapping requirements across internal services
- Implement manual string concatenation and custom regular expression parsing for JSON payloads (rejected)
  Rejected because: Highly error-prone, vulnerable to formatting discrepancies, and fails to handle JSON escaping reliably
  When valid: Never recommended for production network communication

## Risks

- Runtime parsing failures due to schema changes or missing keys in dynamic JSONObject calls
  Mitigation: Encapsulate parsing routines within dedicated adapters with defensive key presence checks and explicit fallback mechanisms.
  Owner: Engineering Team
- Boilerplate proliferation when mapping nested JSON payloads to domain data structures
  Mitigation: Define concise extraction utility helpers and strict domain factory functions.
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
- Construct request bodies using JSONObject and populate array properties using JSONArray before serializing to network streams.
- Isolate all org.json references within the integration boundary to keep domain models decoupled from raw JSON primitives.

## Continuation Context


Verify commands:
- Discover and run the project dependency verification command to validate library resolution.
- Discover and run the project static analysis and test suite to ensure all network integration tests pass.

Accept when:
- All unit and integration tests covering network clients pass successfully.
- Static analysis confirms no unauthorized JSON parsing libraries are introduced in integration modules.

## Enforcement

- Verified by: Automated continuous integration build checks
- Verified by: Peer code review of network client changes and integration pull requests
- Violation handling: Pull requests introducing alternative unapproved JSON libraries or improper parsing boundaries are blocked until updated to adhere to org.json standards.
- Exception process: Submit an architectural proposal detailing why org.json cannot fulfill the specific payload requirements for review by the engineering lead.