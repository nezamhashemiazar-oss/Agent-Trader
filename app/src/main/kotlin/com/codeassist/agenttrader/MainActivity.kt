package com.codeassist.agenttrader

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val messages = remember { mutableStateListOf("CodeAssist agent ready. Type /help.") }
    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    val engine = remember(config) {
        TraderEngine(BitunixRest(config.bitunixApiKey, config.bitunixSecret))
    }

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
                    client.listModels().joinToString("\n").ifBlank { "No models returned." }
                }
                "/set_model" -> {
                    config.model = parsed.args
                    say("Model set to ${parsed.args}")
                }
                else -> say("${parsed.command} is not wired yet.")
            }
            is CommandRouter.Parsed.Trader -> runAsync(post, ::say) { engine.handle(parsed.text) }
            is CommandRouter.Parsed.Chat -> {
                if (config.model.isBlank()) {
                    say("No model set. Use /models then /set_model <name>.")
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
