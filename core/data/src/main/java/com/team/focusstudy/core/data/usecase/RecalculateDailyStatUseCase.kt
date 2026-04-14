package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.StatsRepository
import com.team.focusstudy.core.model.stat.DailyStat
import javax.inject.Inject

class RecalculateDailyStatUseCase @Inject constructor(
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(userId: String, statDate: String): AppResult<DailyStat> {
        return statsRepository.recalculateDailyStat(userId, statDate)
    }
}