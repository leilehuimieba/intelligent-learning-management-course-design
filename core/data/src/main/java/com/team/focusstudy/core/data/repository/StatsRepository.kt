package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.model.stat.DailyStat
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun observeDailyStat(userId: String, statDate: String): Flow<DailyStat?>
    fun observeRange(userId: String, fromDate: String, toDate: String): Flow<List<DailyStat>>
    suspend fun recalculateDailyStat(userId: String, statDate: String): AppResult<DailyStat>
}