package com.codeassist.agenttrader.trader

import com.codeassist.agenttrader.bitunix.Candle
import kotlin.math.abs

/** Aggregated view of one timeframe. dir is 1, -1, or 0 when nothing qualifies. */
data class TfSignal(val dir: Int, val confidence: Double, val agreeing: Int)

/** Final trade decision after checking every timeframe. */
data class Signal(val dir: Int, val confidence: Double, val timeframes: List<String>)

object SignalAggregator {
    const val MIN_AGREEING = 3
    const val MIN_CONFIDENCE = 70.0
    const val TIMEFRAME_CONFIDENCE = 60.0

    /** Timeframes that must agree. Intervals match the Bitunix kline parameter. */
    val TIMEFRAMES = listOf("15m", "1h")

    fun timeframe(candles: List<Candle>): TfSignal {
        val votes = StrategyRegistry.all.map { it.vote(candles) }.filter { it.dir != 0 }
        var best = TfSignal(0, 0.0, 0)
        for (dir in listOf(1, -1)) {
            val mine = votes.filter { it.dir == dir }
            if (mine.size < MIN_AGREEING) continue
            val conf = mine.map { it.confidence }.average()
            if (conf > best.confidence) best = TfSignal(dir, conf, mine.size)
        }
        return best
    }

    /** Combines the timeframes. Every timeframe must pass the timeframe gate and point the same way. */
    fun combine(perTf: Map<String, TfSignal>): Signal? {
        if (perTf.size < TIMEFRAMES.size) return null
        val sigs = TIMEFRAMES.map { perTf[it] ?: return null }
        if (sigs.any { it.dir == 0 || it.confidence < TIMEFRAME_CONFIDENCE }) return null
        if (sigs.map { it.dir }.distinct().size != 1) return null
        val conf = sigs.map { it.confidence }.average()
        if (conf < MIN_CONFIDENCE) return null
        return Signal(sigs[0].dir, conf, TIMEFRAMES)
    }
}

/**
 * TP/SL rules from the user's spec, expressed as prices.
 *  - Stop-loss sits halfway (50%) between entry and the liquidation price, so the loss stays
 *    well inside the liquidation distance. Liquidation is approximated as entry * (1 -/+ 1/leverage).
 *  - Partial take-profit ladder: 1R, 2R, 3R, 4R, each closing 25%. R is the entry-to-stop distance.
 *  - Breakeven: once ROI reaches 10%, the stop moves to entry.
 *  - Trailing stop: once in profit, the stop trails the best price at a 15% ROI distance.
 */
data class TpSlPlan(
    val entry: Double,
    val stop: Double,
    val liquidation: Double,
    val ladder: List<Pair<Double, Double>>, // (price, fraction of position)
)

object TpSlPlanner {
    val LADDER_R = listOf(1.0, 2.0, 3.0, 4.0)
    const val LADDER_FRACTION = 0.25
    const val BREAKEVEN_ROI = 0.10
    const val TRAIL_ROI = 0.15
    const val STOP_FRACTION_OF_LIQ = 0.5

    fun plan(entry: Double, dir: Int, leverage: Int): TpSlPlan {
        val liqDist = entry / leverage
        val liq = if (dir > 0) entry - liqDist else entry + liqDist
        val stop = if (dir > 0) entry - STOP_FRACTION_OF_LIQ * (entry - liq)
        else entry + STOP_FRACTION_OF_LIQ * (liq - entry)
        val r = abs(entry - stop)
        val ladder = LADDER_R.map { k -> (if (dir > 0) entry + k * r else entry - k * r) to LADDER_FRACTION }
        return TpSlPlan(entry, stop, liq, ladder)
    }

    /** ROI of a position at a price, including leverage. */
    fun roi(entry: Double, price: Double, dir: Int, leverage: Int): Double =
        dir * (price - entry) / entry * leverage

    /** Trailing stop price for the best price seen so far. */
    fun trailStop(best: Double, dir: Int, leverage: Int): Double {
        val dist = TRAIL_ROI / leverage
        return if (dir > 0) best * (1 - dist) else best * (1 + dist)
    }
}
