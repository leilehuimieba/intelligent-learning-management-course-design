package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.DailyStatEntity
import com.team.focusstudy.core.model.stat.DailyStat

fun DailyStatEntity.asModel(): DailyStat {
    return DailyStat(
        userId = userId,
        statDate = statDate,
        totalFocusMinutes = totalFocusMinutes,
        totalSessions = totalSessions,
        completedSessions = completedSessions,
        completedTasks = completedTasks,
        plannedTasks = plannedTasks,
        interruptionCount = interruptionCount,
        consistencyDays = consistencyDays,
        reachRate = reachRate,
        completionRate = completionRate,
        lowInterruptRate = lowInterruptRate,
        consistencyRate = consistencyRate,
        focusScore = focusScore,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun DailyStat.asEntity(): DailyStatEntity {
    return DailyStatEntity(
        userId = userId,
        statDate = statDate,
        totalFocusMinutes = totalFocusMinutes,
        totalSessions = totalSessions,
        completedSessions = completedSessions,
        completedTasks = completedTasks,
        plannedTasks = plannedTasks,
        interruptionCount = interruptionCount,
        consistencyDays = consistencyDays,
        reachRate = reachRate,
        completionRate = completionRate,
        lowInterruptRate = lowInterruptRate,
        consistencyRate = consistencyRate,
        focusScore = focusScore,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}