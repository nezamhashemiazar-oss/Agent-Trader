package com.codeassist.agenttrader.agent

/**
 * Parses slash commands typed in the chat. Agent commands are handled here; trader-engine
 * commands are forwarded to the trader engine by the caller.
 */
object CommandRouter {

    val AGENT_COMMANDS: List<Pair<String, String>> = listOf(
        "/help" to "Show this command list",
        "/models" to "List models found for your API key",
        "/set_model <name>" to "Choose the model to use",
        "/connect_provider" to "Set the base URL and API key from .env",
        "/add_mcp <name> <url>" to "Register an MCP server",
        "/mcp" to "List MCP servers",
        "/add_skill <name> <text>" to "Add a skill",
        "/skill <name>" to "Run or show a skill",
        "/add_soul <text>" to "Set the agent persona (soul)",
        "/soul" to "Show the current soul",
        "/think" to "Show the agent's plan for the last task",
        "/memory" to "Show saved project notes",
        "/code_review" to "Review the open file and propose fixes",
        "/bug_fix" to "Find and fix a bug (asks before each edit)",
        "/verify" to "Build and run to verify the last change",
        "/simplify" to "Simplify the selected code",
        "/team_spawn" to "Start sub-agents for a larger task",
        "/check_ai" to "Check the model connection and limits",
        "/check_system" to "Check the trader engine, scanner and agent code",
        "/autopsy" to "Analyse the last failure step by step",
        "/diagnose" to "Collect logs and diagnostics for an issue",
    )

    sealed class Parsed {
        data class Agent(val command: String, val args: String) : Parsed()
        data class Trader(val text: String) : Parsed()
        data class Chat(val text: String) : Parsed()
    }

    private val traderPrefixes = listOf(
        "start trading", "stop trading", "engine on", "engine off", "status", "report on",
        "report off", "report interval", "trades_history", "tpsl_history", "pnl", "settings trade",
        "scan on", "scan off", "close position", "positions history", "mid management",
    )

    fun parse(input: String): Parsed {
        val text = input.trim()
        if (text.startsWith("/")) {
            val parts = text.split(Regex("\\s+"), limit = 2)
            return Parsed.Agent(parts[0].lowercase(), parts.getOrElse(1) { "" })
        }
        val lower = text.lowercase()
        if (traderPrefixes.any { lower.startsWith(it) }) return Parsed.Trader(text)
        return Parsed.Chat(text)
    }

    fun helpText(): String = AGENT_COMMANDS.joinToString("\n") { (cmd, desc) -> "$cmd — $desc" }
}
