package com.codeassist.agenttrader.bitunix

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.UUID

/**
 * Bitunix futures REST client, following the signature steps in the official docs:
 *   digest = SHA256(nonce + timestamp + apiKey + queryParams + body)
 *   sign   = SHA256(digest + secretKey)
 * queryParams are sorted by key and concatenated as key+value; body is compact JSON.
 */
class BitunixRest(private val apiKey: String, private val secretKey: String) {

    private val base = "https://fapi.bitunix.com"

    /** Public GET, no signature. */
    fun publicGet(path: String, query: Map<String, String> = emptyMap()): JSONObject =
        call("GET", path, query, null, signed = false)

    /** Signed GET. */
    fun signedGet(path: String, query: Map<String, String> = emptyMap()): JSONObject =
        call("GET", path, query, null, signed = true)

    /** Signed POST with a JSON body. */
    fun signedPost(path: String, body: JSONObject): JSONObject =
        call("POST", path, emptyMap(), body.toString(), signed = true)

    private fun call(
        method: String,
        path: String,
        query: Map<String, String>,
        body: String?,
        signed: Boolean,
    ): JSONObject {
        val qs = query.toSortedMap().entries.joinToString("&") { "${it.key}=${it.value}" }
        val url = URL(base + path + if (qs.isNotEmpty()) "?$qs" else "")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = method
        conn.connectTimeout = 15_000
        conn.readTimeout = 30_000
        conn.setRequestProperty("language", "en-US")
        conn.setRequestProperty("Content-Type", "application/json")
        if (signed) {
            val nonce = UUID.randomUUID().toString().replace("-", "")
            val timestamp = System.currentTimeMillis().toString()
            val queryParams = query.toSortedMap().entries.joinToString("") { it.key + it.value }
            val digest = sha256Hex(nonce + timestamp + apiKey + queryParams + (body ?: ""))
            val sign = sha256Hex(digest + secretKey)
            conn.setRequestProperty("api-key", apiKey)
            conn.setRequestProperty("nonce", nonce)
            conn.setRequestProperty("timestamp", timestamp)
            conn.setRequestProperty("sign", sign)
        }
        if (body != null) {
            conn.doOutput = true
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
        }
        val code = conn.responseCode
        val text = (if (code in 200..299) conn.inputStream else conn.errorStream)
            ?.bufferedReader()?.use { it.readText() }.orEmpty()
        if (code !in 200..299) throw IllegalStateException("Bitunix HTTP $code: ${text.take(200)}")
        val json = JSONObject(text)
        if (json.optInt("code", 0) != 0) {
            throw IllegalStateException("Bitunix error ${json.optInt("code")}: ${json.optString("msg")}")
        }
        return json
    }

    /** Kline history. Returns candles oldest first; the list excludes the forming candle. */
    fun klines(symbol: String, interval: String, limit: Int = 100): List<Candle> {
        val json = publicGet("/api/v1/futures/market/kline", mapOf(
            "symbol" to symbol, "interval" to interval, "limit" to limit.toString(),
        ))
        val arr: JSONArray = json.optJSONArray("data") ?: return emptyList()
        return (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            Candle(
                time = o.getLong("time"),
                open = o.getString("open").toDouble(),
                high = o.getString("high").toDouble(),
                low = o.getString("low").toDouble(),
                close = o.getString("close").toDouble(),
                volume = o.getString("baseVol").toDouble(),
            )
        }.sortedBy { it.time }
    }

    /** Ticker list used by the scanner (lastPrice, quoteVol, open, high, low). */
    fun tickers(symbols: String = ""): JSONArray {
        val q = if (symbols.isBlank()) emptyMap() else mapOf("symbols" to symbols)
        return publicGet("/api/v1/futures/market/tickers", q).optJSONArray("data") ?: JSONArray()
    }

    fun changeLeverage(symbol: String, marginCoin: String, leverage: Int): JSONObject =
        signedPost("/api/v1/futures/account/change_leverage", JSONObject()
            .put("symbol", symbol).put("marginCoin", marginCoin).put("leverage", leverage))

    fun changeMarginMode(symbol: String, marginCoin: String, mode: String): JSONObject =
        signedPost("/api/v1/futures/account/change_margin_mode", JSONObject()
            .put("symbol", symbol).put("marginCoin", marginCoin).put("marginMode", mode))

    fun changePositionMode(mode: String): JSONObject =
        signedPost("/api/v1/futures/account/change_position_mode", JSONObject().put("positionMode", mode))

    /**
     * Places a market order. Caller must have checked the live gate first.
     * tradeSide ("OPEN"/"CLOSE") is only sent in hedge mode; pass null in one-way mode.
     */
    fun placeMarketOrder(
        symbol: String, qty: String, side: String, tradeSide: String?,
        tpPrice: String?, slPrice: String?, reduceOnly: Boolean = false,
    ): JSONObject {
        val body = JSONObject()
            .put("symbol", symbol).put("qty", qty).put("side", side)
            .put("orderType", "MARKET").put("effect", "GTC")
        if (tradeSide != null) body.put("tradeSide", tradeSide)
        if (reduceOnly) body.put("reduceOnly", true)
        if (tpPrice != null) body.put("tpPrice", tpPrice).put("tpStopType", "LAST_PRICE")
        if (slPrice != null) body.put("slPrice", slPrice).put("slStopType", "LAST_PRICE")
        return signedPost("/api/v1/futures/trade/place_order", body)
    }

    companion object {
        fun sha256Hex(input: String): String =
            MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
                .joinToString("") { "%02x".format(it) }
    }
}

data class Candle(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
)
