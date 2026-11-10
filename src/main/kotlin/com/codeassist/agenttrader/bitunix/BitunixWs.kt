package com.codeassist.agenttrader.bitunix

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Bitunix futures WebSocket client.
 *
 * Public:  wss://fapi.bitunix.com/public/   (market data, no login)
 * Private: wss://fapi.bitunix.com/private/  (login first, then order / position / tpsl / balance)
 *
 * Limits from the docs: at most 5 messages per second per connection (ping and subscribe count),
 * and at most 300 channel subscriptions per connection. Exceeding the limit disconnects the socket.
 */
class BitunixWs(
    private val apiKey: String,
    private val secretKey: String,
    private val listener: Listener,
) {
    interface Listener {
        fun onOpen(privateChannel: Boolean)
        fun onChannel(ch: String, symbol: String?, data: JSONObject)
        fun onError(message: String)
        fun onClosed(privateChannel: Boolean, reason: String)
    }

    private val client = OkHttpClient.Builder()
        .pingInterval(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private var publicSocket: WebSocket? = null
    private var privateSocket: WebSocket? = null
    // One queue per socket: requests made before a socket is ready are sent once it is ready.
    private val pendingPublic = mutableListOf<Pair<String, JSONArray>>()
    private val pendingPrivate = mutableListOf<Pair<String, JSONArray>>()

    fun connectPublic() {
        publicSocket = client.newWebSocket(
            Request.Builder().url(PUBLIC_URL).build(),
            socketListener(privateChannel = false),
        )
    }

    /** Opens the private socket. Subscriptions are sent only after the login reply succeeds. */
    fun connectPrivate() {
        privateSocket = client.newWebSocket(
            Request.Builder().url(PRIVATE_URL).build(),
            socketListener(privateChannel = true),
        )
    }

    /** Subscribe a public market channel, e.g. ("market_kline_15min", "BTCUSDT"). */
    fun subscribePublic(ch: String, symbol: String) = send(publicSocket, pendingPublic, "subscribe", ch, symbol)

    fun unsubscribePublic(ch: String, symbol: String) = send(publicSocket, pendingPublic, "unsubscribe", ch, symbol)

    /** Private channels: "order", "position", "tpsl", "balance" (no symbol). */
    fun subscribePrivate(ch: String) = send(privateSocket, pendingPrivate, "subscribe", ch, null)

    fun close() {
        publicSocket?.close(1000, "bye")
        privateSocket?.close(1000, "bye")
        client.dispatcher.executorService.shutdown()
    }

    private fun socketListener(privateChannel: Boolean) = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            if (privateChannel) {
                login(webSocket)
            } else {
                listener.onOpen(false)
                flush(webSocket, pendingPublic)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val msg = try {
                JSONObject(text)
            } catch (e: Exception) {
                listener.onError("Bad frame: ${text.take(120)}")
                return
            }
            when (msg.optString("op")) {
                "pong" -> Unit
                "ping" -> webSocket.send(JSONObject().put("op", "pong").put("pong", msg.optLong("ping")).toString())
                "login" -> {
                    val ok = msg.optInt("code", -1) == 0
                    if (ok) {
                        listener.onOpen(true)
                        flush(webSocket, pendingPrivate)
                    } else {
                        listener.onError("Login failed: ${msg.optString("msg")}")
                    }
                }
                else -> {
                    val ch = msg.optString("ch")
                    if (ch.isNotEmpty() && msg.has("data")) {
                        val data = msg.opt("data")
                        // Kline docs say data is a list, but the example shows an object. Accept both.
                        val obj = when (data) {
                            is JSONObject -> data
                            is JSONArray -> if (data.length() > 0) data.optJSONObject(0) ?: JSONObject() else JSONObject()
                            else -> JSONObject()
                        }
                        listener.onChannel(ch, msg.optString("symbol").ifEmpty { null }, obj)
                    }
                }
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            listener.onError(t.message ?: t.javaClass.simpleName)
            listener.onClosed(privateChannel, "failure")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            listener.onClosed(privateChannel, "closed $code $reason")
        }
    }

    private fun login(ws: WebSocket) {
        val nonce = UUID.randomUUID().toString().replace("-", "").take(32)
        val timestamp = System.currentTimeMillis() / 1000
        // Private WS login: digest = SHA256(nonce + timestamp + apiKey); sign = SHA256(digest + secretKey)
        val digest = sha256Hex(nonce + timestamp + apiKey)
        val sign = sha256Hex(digest + secretKey)
        val args = JSONArray().put(
            JSONObject()
                .put("apiKey", apiKey)
                .put("timestamp", timestamp)
                .put("nonce", nonce)
                .put("sign", sign),
        )
        ws.send(JSONObject().put("op", "login").put("args", args).toString())
    }

    private fun send(ws: WebSocket?, queue: MutableList<Pair<String, JSONArray>>, op: String, ch: String, symbol: String?) {
        val arg = JSONObject().put("ch", ch)
        if (symbol != null) arg.put("symbol", symbol)
        val args = JSONArray().put(arg)
        if (ws == null) {
            queue.add(op to args)
            return
        }
        ws.send(JSONObject().put("op", op).put("args", args).toString())
    }

    private fun flush(ws: WebSocket, queue: MutableList<Pair<String, JSONArray>>) {
        queue.forEach { (op, args) ->
            ws.send(JSONObject().put("op", op).put("args", args).toString())
        }
        queue.clear()
    }

    companion object {
        const val PUBLIC_URL = "wss://fapi.bitunix.com/public/"
        const val PRIVATE_URL = "wss://fapi.bitunix.com/private/"

        fun sha256Hex(input: String): String =
            MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
                .joinToString("") { "%02x".format(it) }
    }
}
