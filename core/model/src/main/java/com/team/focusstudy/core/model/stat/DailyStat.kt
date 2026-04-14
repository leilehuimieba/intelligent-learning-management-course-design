package com.team.focusstudy.core.model.stat

data class DailyStat(
    val userId: String,
    val statDate: String,
    val totalFocusMinutes: Int,
    val totalSessions: Int,
    val completedSessions: Int,
    val completedTasks: Int,
    val plannedTasks: Int,
    val interruptionCount: Int,
    val consistencyDays: Int,
    val reachRate: Double,
    val completionRate: Double,
    val lowInterruptRate: Double,
    val consistencyRate: Double,
    val focusScore: Double,
    val createdAt: Long,
    val updatedAt: Long
)