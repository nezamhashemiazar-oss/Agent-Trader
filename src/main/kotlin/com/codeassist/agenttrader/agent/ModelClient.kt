package com.codeassist.agenttrader.agent

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Minimal OpenAI-compatible chat client. Model discovery calls GET {base}/models and then
 * sends a tiny test request to each candidate, keeping the ones the key can actually use.
 */
class ModelClient(private val baseUrl: String, private val apiKey: String) {

    data class Probe(val model: String, val ok: Boolean, val detail: String)

    fun listModels(): List<String> {
        val json = request("GET", "$baseUrl/models", null)
        val data: JSONArray = JSONObject(json).optJSONArray("data") ?: return emptyList()
        return (0 until data.length()).map { data.getJSONObject(it).getString("id") }
    }

    /** Tries each model with a one-token request and returns every model with its result. */
    fun autoSetModels(candidates: List<String>): List<Probe> = candidates.map { model ->
        try {
            chat(model, listOf("user" to "ping"), maxTokens = 1)
            Probe(model, true, "ok")
        } catch (e: Exception) {
            Probe(model, false, e.message ?: e.javaClass.simpleName)
        }
    }

    fun chat(model: String, messages: List<Pair<String, String>>, maxTokens: Int = 1024): String {
        val body = JSONObject()
            .put("model", model)
            .put("max_tokens", maxTokens)
            .put(
                "messages",
                JSONArray().also { arr ->
                    messages.forEach { (role, content) ->
                        arr.put(JSONObject().put("role", role).put("content", content))
                    }
                },
            )
        val json = request("POST", "$baseUrl/chat/completions", body.toString())
        return JSONObject(json).getJSONArray("choices").getJSONObject(0)
            .getJSONObject("message").getString("content")
    }

    private fun request(method: String, url: String, body: String?): String {
        require(baseUrl.isNotBlank()) { "Base URL is not set. Use /connect_provider." }
        require(apiKey.isNotBlank()) { "API key is not set. Use /connect_provider." }
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = method
        conn.setRequestProperty("Authorization", "Bearer $apiKey")
        conn.setRequestProperty("Content-Type", "application/json")
        conn.connectTimeout = 15_000
        conn.readTimeout = 60_000
        if (body != null) {
            conn.doOutput = true
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
        }
        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val text = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
        if (code !in 200..299) throw IllegalStateException("HTTP $code: ${text.take(200)}")
        return text
    }
}
