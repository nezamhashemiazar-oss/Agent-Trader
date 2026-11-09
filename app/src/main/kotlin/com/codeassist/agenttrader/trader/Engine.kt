package com.codeassist.agenttrader.trader

import com.codeassist.agenttrader.bitunix.BitunixRest
import com.codeassist.agenttrader.bitunix.Candle

/** An open position, paper or live. dir is 1 for long and -1 for short. */
data class Position(
    val symbol: String,
    val dir: Int,
    val entry: Double,
    var qty: Double,
    val leverage: Int,
    val plan: TpSlPlan,
    var stop: Double,
    var best: Double,
    var nextRung: Int = 0,
    var breakeven: Boolean = false,
)

/**
 * Paper broker: simulated market fills and PnL. Default mode; no exchange calls.
 * Balance is in USDT. Each trade risks [MARGIN_FRACTION] of the balance as margin.
 */
class PaperBroker(var balance: Double = 1000.0) {
    val positions = LinkedHashMap<String, Position>()
    var realizedPnl = 0.0
    val history = mutableListOf<String>()

    fun open(symbol: String, dir: Int, price: Double, leverage: Int): Position? {
        if (positions.containsKey(symbol)) return null
        val margin = balance * MARGIN_FRACTION
        val qty = margin * leverage / price
        val plan = TpSlPlanner.plan(price, dir, leverage)
        val pos = Position(symbol, dir, price, qty, leverage, plan, plan.stop, price)
        positions[symbol] = pos
        history.add("OPEN ${if (dir > 0) "LONG" else "SHORT"} $symbol @ ${fmt(price)} qty ${fmt(qty)}")
        return pos
    }

    /** Walks the ladder and trailing/breakeven rules with a new price. Returns closes done. */
    fun manage(symbol: String, price: Double): List<String> {
        val p = positions[symbol] ?: return emptyList()
        val events = mutableListOf<String>()
        if (p.dir > 0) p.best = maxOf(p.best, price) else p.best = minOf(p.best, price)
        // Partial take-profit ladder
        while (p.nextRung < p.plan.ladder.size) {
            val (target, frac) = p.plan.ladder[p.nextRung]
            val hit = if (p.dir > 0) price >= target else price <= target
            if (!hit) break
            realize(p, target, frac)
            p.nextRung++
            events.add("TP${p.nextRung} $symbol @ ${fmt(target)}")
        }
        if (positions[symbol] == null) return events
        // Breakeven once ROI reaches 10%
        if (!p.breakeven && TpSlPlanner.roi(p.entry, p.best, p.dir, p.leverage) >= TpSlPlanner.BREAKEVEN_ROI) {
            p.breakeven = true
            p.stop = p.entry
        }
        // Trailing stop once in profit
        if (p.breakeven) {
            val trail = TpSlPlanner.trailStop(p.best, p.dir, p.leverage)
            if (p.dir > 0) p.stop = maxOf(p.stop, trail) else p.stop = minOf(p.stop, trail)
        }
        val stopHit = if (p.dir > 0) price <= p.stop else price >= p.stop
        if (stopHit) {
            events.add(close(symbol, p.stop) ?: "")
        }
        return events.filter { it.isNotEmpty() }
    }

    fun close(symbol: String, price: Double): String? {
        val p = positions[symbol] ?: return null
        realize(p, price, 1.0)
        return "CLOSE $symbol @ ${fmt(price)}"
    }

    private fun realize(p: Position, price: Double, fraction: Double) {
        val qty = p.qty * fraction
        val pnl = p.dir * (price - p.entry) * qty
        realizedPnl += pnl
        balance += pnl
        p.qty -= qty
        history.add("PARTIAL ${p.symbol} ${fmt(pnl)} USDT")
        if (p.qty <= 1e-12 || fraction >= 1.0) positions.remove(p.symbol)
    }

    fun unrealized(prices: Map<String, Double>): Double = positions.values.sumOf { p ->
        val px = prices[p.symbol] ?: p.entry
        p.dir * (px - p.entry) * p.qty
    }

    companion object {
        const val MARGIN_FRACTION = 0.10
    }
}

fun fmt(v: Double): String = "%.4f".format(v)

/**
 * Trader engine: owns the broker, the scanner universe, and the on/off switches.
 * Live trading needs explicit enabling and API keys. Until then every order is paper.
 */
class TraderEngine(private val rest: BitunixRest) {
    val broker = PaperBroker()
    var engineOn = false
    var trading = false
    var scanOn = false
    var live = false
    var leverage = 5
    var marginMode = "ISOLATION"
    var positionMode = "ONE_WAY"
    val universe = mutableListOf("BTCUSDT", "ETHUSDT", "SOLUSDT")

    private fun candles(symbol: String, tf: String): List<Candle> = rest.klines(symbol, tf, 200)

    /** Ranks the scanner universe by quote volume and returns the top symbols. */
    fun rankUniverse(top: Int = 10): List<String> {
        val arr = rest.tickers()
        val rows = (0 until arr.length()).map { arr.getJSONObject(it) }
            .filter { it.optString("symbol").endsWith("USDT") }
            .sortedByDescending { it.optString("quoteVol").toDoubleOrNull() ?: 0.0 }
        return rows.take(top).map { it.getString("symbol") }
    }

    /** One scan cycle: analyse each symbol and open a paper trade when the signal passes. */
    fun scanOnce(): List<String> {
        val out = mutableListOf<String>()
        val symbols = if (scanOn) rankUniverse() else universe.toList()
        for (sym in symbols) {
            if (broker.positions.containsKey(sym)) continue
            val perTf = SignalAggregator.TIMEFRAMES.associateWith { SignalAggregator.timeframe(candles(sym, it)) }
            val sig = SignalAggregator.combine(perTf) ?: continue
            val price = candles(sym, SignalAggregator.TIMEFRAMES.first()).last().close
            if (live) {
                out.add("LIVE orders are gated: $sym signal ${sig.dir} conf ${"%.0f".format(sig.confidence)} (not sent)")
                continue
            }
            broker.open(sym, sig.dir, price, leverage)?.let {
                out.add("OPEN ${if (sig.dir > 0) "LONG" else "SHORT"} $sym @ ${fmt(price)} conf ${"%.0f".format(sig.confidence)}")
            }
        }
        return out
    }

    /** Manages open positions against current prices. */
    fun tick(): List<String> {
        val events = mutableListOf<String>()
        for (sym in broker.positions.keys.toList()) {
            val price = candles(sym, "15m").lastOrNull()?.close ?: continue
            events.addAll(broker.manage(sym, price))
        }
        return events
    }

    fun status(): String = buildString {
        appendLine("Engine: ${if (engineOn) "on" else "off"}, trading: ${if (trading) "on" else "off"}, scan: ${if (scanOn) "on" else "off"}")
        appendLine("Mode: ${if (live) "LIVE" else "PAPER"}, leverage ${leverage}x, margin $marginMode, position $positionMode")
        appendLine("Balance: ${fmt(broker.balance)} USDT, open positions: ${broker.positions.size}")
    }

    fun pnl(): String =
        "Realized PnL: ${fmt(broker.realizedPnl)} USDT. Balance: ${fmt(broker.balance)} USDT."

    fun closePosition(symbol: String): String {
        val price = candles(symbol, "15m").lastOrNull()?.close ?: return "No price for $symbol."
        return broker.close(symbol, price) ?: "No open position on $symbol."
    }

    /** Handles one trader command typed in chat. Returns the reply text. */
    fun handle(text: String): String {
        val t = text.trim().lowercase()
        return when {
            t.startsWith("engine on") -> { engineOn = true; "Engine on (paper mode)." }
            t.startsWith("engine off") -> { engineOn = false; trading = false; "Engine off." }
            t.startsWith("start trading") -> {
                if (!engineOn) "Turn the engine on first: engine on"
                else { trading = true; "Trading started (paper)." }
            }
            t.startsWith("stop trading") -> { trading = false; "Trading stopped." }
            t.startsWith("scan on") -> { scanOn = true; "Scanner on: ranking the top symbols by volume." }
            t.startsWith("scan off") -> { scanOn = false; "Scanner off: using the fixed universe." }
            t.startsWith("status") -> status()
            t.startsWith("pnl") -> pnl()
            t.startsWith("close position") -> closePosition(t.removePrefix("close position").trim().uppercase().ifEmpty { "BTCUSDT" })
            else -> "Not supported yet: $text"
        }
    }
}
