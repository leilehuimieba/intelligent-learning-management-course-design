package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.AdviceRepository
import com.team.focusstudy.core.model.advice.AdviceLog
import javax.inject.Inject

class GenerateAdviceUseCase @Inject constructor(
    private val adviceRepository: AdviceRepository
) {
    suspend operator fun invoke(userId: String, statDate: String): AppResult<List<AdviceLog>> {
        return adviceRepository.generateAdvice(userId, statDate)
    }
}