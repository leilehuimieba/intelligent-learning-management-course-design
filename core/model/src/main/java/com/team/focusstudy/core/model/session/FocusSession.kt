package com.team.focusstudy.core.model.session

enum class SessionStatus {
    COMPLETED,
    INTERRUPTED,
    CANCELLED
}

enum class FocusState {
    IDLE,
    RUNNING,
    PAUSED,
    FINISHED
}

enum class DistractionType {
    APP_SWITCH,
    NOTIFICATION,
    MANUAL_PAUSE,
    PHONE_CALL,
    OTHER
}

data class FocusSession(
    val id: String,
    val userId: String,
    val taskId: String?,
    val startTime: Long,
    val endTime: Long?,
    val plannedMinutes: Int,
    val actualMinutes: Int,
    val pauseCount: Int,
    val interruptCount: Int,
    val sessionStatus: SessionStatus,
    val summaryNote: String?,
    val source: String,
    val createdAt: Long
)

data class DistractionEvent(
    val id: String,
    val sessionId: String,
    val eventType: DistractionType,
    val eventTime: Long,
    val durationSeconds: Int,
    val detail: String?
)