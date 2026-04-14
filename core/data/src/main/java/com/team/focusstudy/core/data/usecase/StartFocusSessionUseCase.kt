package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.FocusSessionRepository
import com.team.focusstudy.core.data.repository.ActiveSession
import javax.inject.Inject

class StartFocusSessionUseCase @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository
) {
    suspend operator fun invoke(
        userId: String,
        taskId: String?,
        plannedMinutes: Int,
        startTime: Long
    ): AppResult<ActiveSession> {
        return focusSessionRepository.startSession(
            userId = userId,
            taskId = taskId,
            plannedMinutes = plannedMinutes,
            startTime = startTime
        )
    }
}