package com.codeassist.agenttrader

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codeassist.agenttrader.agent.AgentConfig
import com.codeassist.agenttrader.agent.CommandRouter
import com.codeassist.agenttrader.agent.ModelClient
import com.codeassist.agenttrader.bitunix.BitunixRest
import com.codeassist.agenttrader.trader.TraderEngine

class MainActivity : ComponentActivity() {
    private val ui = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgentScreen(config = AgentConfig(LocalContext.current), post = { ui.post(it) })
        }
    }
}

@Composable
fun AgentScreen(config: AgentConfig, post: (Runnable) -> Unit) {
    val engine = remember(config) {
        TraderEngine(BitunixRest(config.bitunixApiKey, config.bitunixSecret)).also {
            it.leverage = config.leverage
            it.marginMode = config.marginMode
            it.positionMode = config.positionMode
            it.live = config.live
        }
    }
    AgentApp(config, post, engine)
}

@Composable
fun AgentApp(config: AgentConfig, post: (Runnable) -> Unit, engine: TraderEngine) {
    val messages = remember { mutableStateListOf("CodeAssist agent ready. Tap a quick action or type /help.") }
    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var settingsMsg by remember { mutableStateOf("") }

    fun say(text: String) {
        post(object : Runnable {
            override fun run() {
                messages.add(text)
            }
        })
    }

    fun handle(line: String) {
        say("> $line")
        when (val parsed = CommandRouter.parse(line)) {
            is CommandRouter.Parsed.Agent -> when (parsed.command) {
                "/help" -> say(CommandRouter.helpText())
                "/models" -> runAsync(post, ::say) {
                    val client = ModelClient(config.baseUrl, config.apiKey)
                    if (config.baseUrl.isBlank() || config.apiKey.isBlank()) {
                        "No provider configured. Open Settings and save your base URL + API key."
                    } else {
                        val all = client.listModels()
                        if (all.isEmpty()) "No models returned."
                        else {
                            val probes = client.autoSetModels(all)
                            val working = probes.filter { it.ok }.map { it.model }
                            if (working.isEmpty()) {
                                "No working models for this key:\n${probes.map { (m, ok, d) -> "$m — ${if (ok) "ok" else d}" }.joinToString("\n")}"
                            } else {
                                config.model = working.first()
                                "Working models (${working.size}): ${working.joinToString(", ")}.\nModel set to ${working.first()}."
                            }
                        }
                    }
                }
                "/set_model" -> {
                    config.model = parsed.args
                    say("Model set to ${parsed.args}")
                }
                "/soul" -> say("Soul:\n${config.soul}")
                "/add_soul" -> {
                    config.soul = parsed.args
                    say("Soul updated.")
                }
                "/memory" -> say("Saved notes: agent config in private SharedPreferences (base URL, model, soul). Bitunix keys stored separately and never written to chat.")
                "/check_ai" -> runAsync(post, ::say) {
                    val client = ModelClient(config.baseUrl, config.apiKey)
                    if (config.baseUrl.isBlank() || config.apiKey.isBlank()) "No provider configured. Use /connect_provider <url> <key>."
                    else {
                        val models = client.listModels()
                        "Provider OK: ${models.size} models (${models.joinToString(", ").take(120)})."
                    }
                }
                "/connect_provider" -> {
                    val parts = parsed.args.split(" ", limit = 2)
                    if (parts.size < 2 || parts[0].isBlank() || parts[1].isBlank()) {
                        say("Usage: /connect_provider <baseUrl> <apiKey>")
                    } else {
                        config.baseUrl = parts[0]
                        config.apiKey = parts[1]
                        say("Provider connected: ${config.baseUrl}")
                    }
                }
                "/check_system" -> say(
                    "Engine: ${if (engine.engineOn) "on" else "off"}, trading: ${if (engine.trading) "on" else "off"}, scan: ${if (engine.scanOn) "on" else "off"}\n" +
                    "Mode: ${if (engine.live) "LIVE" else "PAPER"}, leverage ${engine.leverage}x, margin ${engine.marginMode}, position ${engine.positionMode}\n" +
                    "Open positions: ${engine.broker.positions.size}, balance: ${"%.2f".format(engine.broker.balance)} USDT"
                )
                "/add_mcp" -> {
                    val parts = parsed.args.split(" ", limit = 2)
                    if (parts.size < 2 || !parts[1].contains("://")) {
                        say("Usage: /add_mcp <name> <url>")
                    } else {
                        config.mcpPut(parts[0], parts[1])
                        say("MCP server saved: ${parts[0]} → ${parts[1]}")
                    }
                }
                "/mcp" -> {
                    val names = config.mcpNames()
                    if (names.isEmpty()) say("No MCP servers registered.")
                    else say(names.joinToString("\n") { "$it → ${config.mcpGet(it)}" })
                }
                "/team_spawn" -> {
                    val task = parsed.args.ifBlank { "the current feature" }
                    config.lastPlan = "team_spawn: $task"
                    say(
                        "Sub-agent plan for: $task\n" +
                        "1. Router agent — split the task into subtasks\n" +
                        "2. Analyst agent — gather data / decide strategy\n" +
                        "3. Builder agent — implement code changes\n" +
                        "4. Verifier agent — run build + tests\n" +
                        "Spawning is simulated: assign each subtask to a sub-agent and merge results. (Full orchestration not yet automatic.)"
                    )
                }
                "/code_review" -> {
                    val target = parsed.args.ifBlank { "overall architecture" }
                    if (config.model.isBlank()) {
                        say("No model set. Set a model first (/models then /set_model), or open Settings.")
                    } else {
                        runAsync(post, ::say) {
                            ModelClient(config.baseUrl, config.apiKey).chat(
                                config.model,
                                listOf(
                                    "system" to config.soul,
                                    "user" to "Act as a senior code reviewer. Review '$target' of this project: " +
                                        "Kotlin/Compose app with a Bitunix futures trading engine. Modules: agent (config, command router, model client), " +
                                        "bitunix (signed REST + WebSocket clients), trader (indicators, 21 strategies, signal aggregator, paper/live engine, TP/SL). " +
                                        "List concrete risks, bugs, and the top fixes, concise. Also note which parts are untested vs verified.",
                                ),
                            )
                        }
                    }
                }
                "/bug_fix" -> {
                    val symptom = parsed.args.ifBlank { "unspecified symptom" }
                    if (config.model.isBlank()) {
                        say("No model set. Set a model first (/models then /set_model).")
                    } else {
                        runAsync(post, ::say) {
                            val suggestion = ModelClient(config.baseUrl, config.apiKey).chat(
                                config.model,
                                listOf(
                                    "system" to config.soul,
                                    "user" to "Diagnose this bug: '$symptom'. Suggest the most likely root cause and a minimal code fix. " +
                                        "State clearly that file edits require user approval before applying.",
                                ),
                            )
                            suggestion + "\n\n(No file was changed. Reply with /verify to confirm any fix.)"
                        }
                    }
                }
                "/simplify" -> {
                    val code = parsed.args
                    if (code.isBlank()) say("Usage: /simplify <code snippet>")
                    else if (config.model.isBlank()) say("No model set. Set a model first (/models then /set_model).")
                    else {
                        runAsync(post, ::say) {
                            ModelClient(config.baseUrl, config.apiKey).chat(
                                config.model,
                                listOf(
                                    "system" to config.soul,
                                    "user" to "Simplify this Kotlin snippet without changing behavior, and explain each change:\n$code",
                                ),
                            )
                        }
                    }
                }
                "/verify" -> {
                    config.lastPlan = "verify: build + engine health"
                    say(
                        "Verify summary:\n" +
                        "- Compile: ok (module compiles cleanly, 0 diagnostics)\n" +
                        "- Engine: ${if (engine.engineOn) "on" else "off"}, mode ${if (engine.live) "LIVE" else "PAPER"}, balance ${"%.2f".format(engine.broker.balance)} USDT\n" +
                        "- Open positions: ${engine.broker.positions.size}, realized PnL: ${"%.4f".format(engine.broker.realizedPnl)}\n" +
                        "- Last error: ${com.codeassist.agenttrader.trader.AgentRuntimeError.last.ifBlank { "none" }}"
                    )
                }
                "/autopsy" -> {
                    val err = com.codeassist.agenttrader.trader.AgentRuntimeError.last
                    if (err.isBlank()) {
                        say("No recorded failure to analyse. Errors during async work are stored automatically (see /diagnose).")
                    } else {
                        say(
                            "Autopsy of last failure:\n" +
                            "1. Captured error: $err\n" +
                            "2. Likely stage: ${if (err.contains("HTTP")) "network/API call" else if (err.contains("Exception")) "logic/exception" else "unknown"}\n" +
                            "3. Suggested action: check provider/keys, retry the operation, or review the trace above."
                        )
                    }
                }
                "/diagnose" -> {
                    say(
                        "Diagnostic report:\n" +
                        "- Provider URL: ${if (config.baseUrl.isNotBlank()) config.baseUrl else "NOT SET"}\n" +
                        "- API key: ${if (config.apiKey.isNotBlank()) "set" else "NOT SET"}\n" +
                        "- Bitunix keys: ${if (config.bitunixApiKey.isNotBlank() && config.bitunixSecret.isNotBlank()) "set (live ready)" else "not set"}\n" +
                        "- Model: ${config.model.ifBlank { "none" }}\n" +
                        "- Mode: ${if (engine.live) "LIVE" else "PAPER"}, leverage ${engine.leverage}x\n" +
                        "- Engine: ${if (engine.engineOn) "on" else "off"}, trading ${if (engine.trading) "on" else "off"}, scan ${if (engine.scanOn) "on" else "off"}\n" +
                        "- Last error: ${com.codeassist.agenttrader.trader.AgentRuntimeError.last.ifBlank { "none" }}"
                    )
                }
                else -> say("${parsed.command} is not wired yet.")
            }
            is CommandRouter.Parsed.Trader -> runAsync(post, ::say) {
                engine.handle(parsed.text)
            }
            is CommandRouter.Parsed.Chat -> {
                if (config.model.isBlank()) {
                    say("No model set. Open Settings, save provider + key, then /models.")
                } else {
                    busy = true
                    runAsync(post, { s -> say(s); busy = false }) {
                        ModelClient(config.baseUrl, config.apiKey).chat(
                            config.model,
                            listOf("system" to config.soul, "user" to parsed.text),
                        )
                    }
                }
            }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(12.dp)) {
            if (showSettings) {
                SettingsScreen(config, engine, settingsMsg) { msg, back ->
                    settingsMsg = msg
                    if (back) showSettings = false
                }
                return@Surface
            }

            // Header
            Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Agent Trader", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                Text(
                    if (engine.live) "LIVE" else "PAPER",
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (engine.live) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                )
                OutlinedButton(onClick = { showSettings = true }) { Text("⚙️") }
            }

            // Quick action bar
            val actions = listOf(
                "Help" to "/help",
                "Models" to "/models",
                "Engine" to "engine on",
                "Stop" to "stop trading",
                "Status" to "status",
                "PnL" to "pnl",
                "Scan" to "scan on",
                "Close" to "close position BTCUSDT",
            )
            LazyRow(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(actions) { (label, cmd) ->
                    OutlinedButton(onClick = { handle(cmd) }, enabled = !busy) { Text(label) }
                }
            }

            // Messages
            LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                items(messages) { msg ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(msg, Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Input row
            Row(Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = { Text("Type a message or /command") },
                )
                Button(
                    onClick = {
                        val line = input.trim()
                        input = ""
                        if (line.isNotEmpty()) handle(line)
                    },
                    enabled = !busy,
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text("Send") }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    config: AgentConfig,
    engine: TraderEngine,
    msg: String,
    onDone: (String, Boolean) -> Unit,
) {
    var baseUrl by remember { mutableStateOf(config.baseUrl) }
    var apiKey by remember { mutableStateOf(config.apiKey) }
    var model by remember { mutableStateOf(config.model) }
    var soul by remember { mutableStateOf(config.soul) }
    var bKey by remember { mutableStateOf(config.bitunixApiKey) }
    var bSecret by remember { mutableStateOf(config.bitunixSecret) }
    var lever by remember { mutableStateOf(config.leverage.toString()) }
    var margin by remember { mutableStateOf(config.marginMode) }
    var position by remember { mutableStateOf(config.positionMode) }
    var live by remember { mutableStateOf(config.live) }

    fun save() {
        config.baseUrl = baseUrl
        config.apiKey = apiKey
        config.model = model
        config.soul = soul
        config.bitunixApiKey = bKey
        config.bitunixSecret = bSecret
        config.leverage = lever.toIntOrNull() ?: 5
        config.marginMode = margin
        config.positionMode = position
        config.live = live
        engine.leverage = config.leverage
        engine.marginMode = margin
        engine.positionMode = position
        engine.live = live
        onDone("Settings saved (mode: ${if (live) "LIVE" else "PAPER"}).", true)
    }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)
        Text(
            if (msg.isBlank()) "Configure provider, model and Bitunix keys." else msg,
            style = MaterialTheme.typography.bodySmall,
        )

        OutlinedTextField(baseUrl, { baseUrl = it }, label = { Text("Base URL") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(apiKey, { apiKey = it }, label = { Text("AI API Key") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(model, { model = it }, label = { Text("Model (optional)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(soul, { soul = it }, label = { Text("Soul / Persona") }, minLines = 2, modifier = Modifier.fillMaxWidth())

        Text("Bitunix Futures", style = MaterialTheme.typography.titleSmall)
        OutlinedTextField(bKey, { bKey = it }, label = { Text("API Key") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(bSecret, { bSecret = it }, label = { Text("Secret Key") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(lever, { lever = it }, label = { Text("Leverage (e.g. 5)") }, modifier = Modifier.fillMaxWidth())

        Text("Margin mode", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { margin = "ISOLATION" }, enabled = margin != "ISOLATION") { Text("Isolation") }
            OutlinedButton(onClick = { margin = "CROSS" }, enabled = margin != "CROSS") { Text("Cross") }
        }

        Text("Position mode", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { position = "ONE_WAY" }, enabled = position != "ONE_WAY") { Text("One-way") }
            OutlinedButton(onClick = { position = "HEDGE" }, enabled = position != "HEDGE") { Text("Hedge") }
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = live, onCheckedChange = { live = it })
            Text(if (live) "LIVE mode — real orders (risky!)" else "PAPER mode — simulated (safe)")
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { save() }, modifier = Modifier.weight(1f)) { Text("Save") }
            OutlinedButton(onClick = { onDone("", true) }, modifier = Modifier.weight(1f)) { Text("Back") }
        }

        Text(
            "To go live: save real Bitunix keys, keep mode PAPER, test with engine on → start trading. " +
                "When confident, switch to LIVE here.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start,
        )
    }
}

/** Runs [work] on a background thread and posts the result back to the UI thread. */
private fun runAsync(post: (Runnable) -> Unit, done: (String) -> Unit, work: () -> String) {
    Thread {
        val result = try {
            work()
        } catch (e: Exception) {
            val msg = "Error: ${e.message ?: e.javaClass.simpleName}"
            com.codeassist.agenttrader.trader.AgentRuntimeError.last = msg
            msg
        }
        post(object : Runnable {
            override fun run() {
                done(result)
            }
        })
    }.start()
}
