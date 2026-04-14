package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.FocusSessionRepository
import com.team.focusstudy.core.model.session.FocusSession
import com.team.focusstudy.core.model.session.SessionStatus
import javax.inject.Inject

class EndFocusSessionUseCase @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository
) {
    suspend operator fun invoke(
        sessionId: String,
        endTime: Long,
        pauseDurationMillis: Long,
        pauseCount: Int,
        interruptCount: Int,
        sessionStatus: SessionStatus,
        summaryNote: String?
    ): AppResult<FocusSession> {
        return focusSessionRepository.endSession(
            sessionId = sessionId,
            endTime = endTime,
            pauseDurationMillis = pauseDurationMillis,
            pauseCount = pauseCount,
            interruptCount = interruptCount,
            sessionStatus = sessionStatus,
            summaryNote = summaryNote
        )
    }
}
