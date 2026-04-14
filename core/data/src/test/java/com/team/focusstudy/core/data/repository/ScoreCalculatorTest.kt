package com.team.focusstudy.core.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreCalculatorTest {

    @Test
    fun `calculate returns bounded score`() {
        val breakdown = ScoreCalculator.calculate(
            totalFocusMinutes = 600,
            dailyTargetMinutes = 120,
            completedTasks = 5,
            plannedTasks = 5,
            interruptionCount = 0,
            totalSessions = 1,
            consistencyDays = 10
        )

        assertEquals(100.0, breakdown.focusScore, 0.001)
    }

    @Test
    fun `calculate gives no interruption bonus when no session exists`() {
        val breakdown = ScoreCalculator.calculate(
            totalFocusMinutes = 0,
            dailyTargetMinutes = 120,
            completedTasks = 0,
            plannedTasks = 0,
            interruptionCount = 0,
            totalSessions = 0,
            consistencyDays = 0
        )

        assertEquals(0.0, breakdown.lowInterruptRate, 0.0)
        assertEquals(0.0, breakdown.focusScore, 0.0)
    }

    @Test
    fun `consistency mapping follows rules`() {
        assertEquals(0.0, ScoreCalculator.consistencyRate(0), 0.0)
        assertEquals(0.4, ScoreCalculator.consistencyRate(2), 0.0)
        assertEquals(0.7, ScoreCalculator.consistencyRate(5), 0.0)
        assertEquals(1.0, ScoreCalculator.consistencyRate(7), 0.0)
    }
}
