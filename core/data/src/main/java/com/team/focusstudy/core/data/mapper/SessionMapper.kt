package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.DistractionEventEntity
import com.team.focusstudy.core.database.entity.FocusSessionEntity
import com.team.focusstudy.core.model.session.DistractionEvent
import com.team.focusstudy.core.model.session.FocusSession

fun FocusSessionEntity.asModel(): FocusSession {
    return FocusSession(
        id = id,
        userId = userId,
        taskId = taskId,
        startTime = startTime,
        endTime = endTime,
        plannedMinutes = plannedMinutes,
        actualMinutes = actualMinutes,
        pauseCount = pauseCount,
        interruptCount = interruptCount,
        sessionStatus = sessionStatus,
        summaryNote = summaryNote,
        source = source,
        createdAt = createdAt
    )
}

fun FocusSession.asEntity(): FocusSessionEntity {
    return FocusSessionEntity(
        id = id,
        userId = userId,
        taskId = taskId,
        startTime = startTime,
        endTime = endTime,
        plannedMinutes = plannedMinutes,
        actualMinutes = actualMinutes,
        pauseCount = pauseCount,
        interruptCount = interruptCount,
        sessionStatus = sessionStatus,
        summaryNote = summaryNote,
        source = source,
        createdAt = createdAt
    )
}

fun DistractionEventEntity.asModel(): DistractionEvent {
    return DistractionEvent(
        id = id,
        sessionId = sessionId,
        eventType = eventType,
        eventTime = eventTime,
        durationSeconds = durationSeconds,
        detail = detail
    )
}

fun DistractionEvent.asEntity(): DistractionEventEntity {
    return DistractionEventEntity(
        id = id,
        sessionId = sessionId,
        eventType = eventType,
        eventTime = eventTime,
        durationSeconds = durationSeconds,
        detail = detail
    )
}