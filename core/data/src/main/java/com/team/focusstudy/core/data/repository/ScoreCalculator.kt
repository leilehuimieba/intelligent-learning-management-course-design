package com.team.focusstudy.core.data.repository

import kotlin.math.max
import kotlin.math.min

internal object ScoreCalculator {

    fun calculate(
        totalFocusMinutes: Int,
        dailyTargetMinutes: Int,
        completedTasks: Int,
        plannedTasks: Int,
        interruptionCount: Int,
        totalSessions: Int,
        consistencyDays: Int
    ): ScoreBreakdown {
        val reachRate = if (dailyTargetMinutes <= 0) 0.0 else min(totalFocusMinutes.toDouble() / dailyTargetMinutes, 1.0)
        val completionRate = if (plannedTasks <= 0) 0.0 else (completedTasks.toDouble() / plannedTasks).coerceIn(0.0, 1.0)
        val lowInterruptRate = if (totalSessions <= 0) {
            0.0
        } else {
            max(1.0 - interruptionCount.toDouble() / totalSessions, 0.0)
        }
        val consistencyRate = consistencyRate(consistencyDays)
        val focusScore = 100.0 * (0.35 * reachRate + 0.30 * completionRate + 0.20 * lowInterruptRate + 0.15 * consistencyRate)

        return ScoreBreakdown(
            reachRate = reachRate,
            completionRate = completionRate,
            lowInterruptRate = lowInterruptRate,
            consistencyRate = consistencyRate,
            focusScore = focusScore.coerceIn(0.0, 100.0)
        )
    }

    fun consistencyRate(consistencyDays: Int): Double {
        return when {
            consistencyDays <= 0 -> 0.0
            consistencyDays in 1..2 -> 0.4
            consistencyDays in 3..6 -> 0.7
            else -> 1.0
        }
    }
}

internal data class ScoreBreakdown(
    val reachRate: Double,
    val completionRate: Double,
    val lowInterruptRate: Double,
    val consistencyRate: Double,
    val focusScore: Double
)
