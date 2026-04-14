package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.model.session.DistractionEvent
import com.team.focusstudy.core.model.session.DistractionType
import com.team.focusstudy.core.model.session.FocusSession
import com.team.focusstudy.core.model.session.SessionStatus
import kotlinx.coroutines.flow.Flow

interface FocusSessionRepository {
    fun observeRecentSessions(userId: String): Flow<List<FocusSession>>
    suspend fun getActiveSession(): ActiveSession?

    suspend fun startSession(
        userId: String,
        taskId: String?,
        plannedMinutes: Int,
        startTime: Long
    ): AppResult<ActiveSession>

    suspend fun updateActiveSession(activeSession: ActiveSession)

    suspend fun recordDistraction(
        sessionId: String,
        eventType: DistractionType,
        detail: String?,
        eventTime: Long,
        durationSeconds: Int
    ): AppResult<DistractionEvent>

    suspend fun endSession(
        sessionId: String,
        endTime: Long,
        pauseDurationMillis: Long,
        pauseCount: Int,
        interruptCount: Int,
        sessionStatus: SessionStatus,
        summaryNote: String?
    ): AppResult<FocusSession>
}

data class ActiveSession(
    val sessionId: String,
    val userId: String,
    val taskId: String?,
    val startTime: Long,
    val plannedMinutes: Int,
    val pauseDurationMillis: Long,
    val pauseCount: Int,
    val interruptCount: Int
)
