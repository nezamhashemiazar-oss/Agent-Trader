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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
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
        TraderEngine(BitunixRest(config.bitunixApiKey, config.bitunixSecret))
    }
    AgentApp(config, post, engine)
}

@Composable
fun AgentApp(config: AgentConfig, post: (Runnable) -> Unit, engine: TraderEngine) {
    val messages = remember { mutableStateListOf("CodeAssist agent ready. Type /help or open Settings.") }
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
                SettingsScreen(config, settingsMsg) { msg, _ ->
                    settingsMsg = msg
                    showSettings = false
                }
                return@Surface
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Agent Trader", Modifier.weight(1f), style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
                OutlinedButton(onClick = { showSettings = true }) { Text("⚙️ Settings") }
            }
            LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                items(messages) { msg -> Text(msg, Modifier.padding(vertical = 4.dp)) }
            }
            Row(Modifier.fillMaxWidth()) {
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
    msg: String,
    onBack: (String, Boolean) -> Unit,
) {
    var baseUrl by remember { mutableStateOf(config.baseUrl) }
    var apiKey by remember { mutableStateOf(config.apiKey) }
    var model by remember { mutableStateOf(config.model) }
    var soul by remember { mutableStateOf(config.soul) }
    var bKey by remember { mutableStateOf(config.bitunixApiKey) }
    var bSecret by remember { mutableStateOf(config.bitunixSecret) }
    var liveByDefault by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Settings", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Text(if (msg.isBlank()) "" else msg, style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        OutlinedTextField(baseUrl, { baseUrl = it }, label = { Text("Base URL") })
        OutlinedTextField(apiKey, { apiKey = it }, label = { Text("AI API Key") })
        OutlinedTextField(model, { model = it }, label = { Text("Model") })
        OutlinedTextField(soul, { soul = it }, label = { Text("Soul / Persona") }, minLines = 2)
        OutlinedTextField(bKey, { bKey = it }, label = { Text("Bitunix API Key") })
        OutlinedTextField(bSecret, { bSecret = it }, label = { Text("Bitunix Secret") })
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = liveByDefault, onCheckedChange = { liveByDefault = it })
            Text("Default live mode after saving (paper recommended)")
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                config.baseUrl = baseUrl
                config.apiKey = apiKey
                config.model = model
                config.soul = soul
                config.bitunixApiKey = bKey
                config.bitunixSecret = bSecret
                onBack("Settings saved.", false)
            }, modifier = Modifier.weight(1f)) { Text("Save") }
            OutlinedButton(onClick = { onBack("", true) }, modifier = Modifier.weight(1f)) { Text("Back") }
        }
        if (bKey.isNotBlank() && bSecret.isNotBlank()) {
            Text("To connect live: start trading → engine on → set leverage/margin/position mode, then start trading.")
        }
    }
}

/** Runs [work] on a background thread and posts the result back to the UI thread. */
private fun runAsync(post: (Runnable) -> Unit, done: (String) -> Unit, work: () -> String) {
    Thread {
        val result = try {
            work()
        } catch (e: Exception) {
            "Error: ${e.message ?: e.javaClass.simpleName}"
        }
        post(object : Runnable {
            override fun run() {
                done(result)
            }
        })
    }.start()
}
