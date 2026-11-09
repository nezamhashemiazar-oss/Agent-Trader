---
name: bitunix-futures-dev
description: Comprehensive Bitunix Futures API development, debugging, fixing, and trading skill. Contains official endpoints, parameters, error codes, liquidation mechanisms, review checklists, and multi-language open-api code examples (Node, Python, Go, Java, PHP) to eliminate guessing.
version: 1.0.0
---
# Bitunix Futures Development & Debugging Skill

This skill provides complete, authoritative documentation, parameters, error codes, liquidation rules, and code examples for the Bitunix Futures API. Use this skill when building, debugging, testing, or fixing any Bitunix trading bot or integration.

## Core Reference Structure

- **API Documentation**: Located in `references/api-docs/` and `references/api-markdown/` covering Account, Market, Trade, Position, TP/SL, CopyTrading, WebSockets, and Error Codes.
- **Parameters & Checklist**: Located in `references/BITUNIX_API_PARAMETERS.md`, `references/official-endpoints-bitunix.md`, and `references/REVIEW_CHECKLIST.md`.
- **Code Examples**: Official open-api reference implementations in Node, Python, Go, Java, and PHP.

## Guidelines for Fixing and Debugging

1. **Never guess endpoints or parameters**: Always cross-reference against `references/api-markdown/` or `BITUNIX_API_PARAMETERS.md`.
2. **Signature & Authentication**: Follow the official sign implementation in the reference examples for timestamp, sign, and headers.
3. **Liquidation & Margin**: Adhere strictly to Bitunix maintenance margin rates and tiered risk limits documented in `liquidation_mechanism.md`.