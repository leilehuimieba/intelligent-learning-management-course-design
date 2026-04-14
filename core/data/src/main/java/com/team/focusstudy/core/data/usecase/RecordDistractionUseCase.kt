package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.FocusSessionRepository
import com.team.focusstudy.core.model.session.DistractionEvent
import com.team.focusstudy.core.model.session.DistractionType
import javax.inject.Inject

class RecordDistractionUseCase @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository
) {
    suspend operator fun invoke(
        sessionId: String,
        eventType: DistractionType,
        detail: String?,
        eventTime: Long,
        durationSeconds: Int
    ): AppResult<DistractionEvent> {
        return focusSessionRepository.recordDistraction(
            sessionId = sessionId,
            eventType = eventType,
            detail = detail,
            eventTime = eventTime,
            durationSeconds = durationSeconds
        )
    }
}