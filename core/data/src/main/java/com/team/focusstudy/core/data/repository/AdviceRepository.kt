package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.model.advice.AdviceLog
import kotlinx.coroutines.flow.Flow

interface AdviceRepository {
    fun observeTodayAdvice(userId: String, statDate: String): Flow<List<AdviceLog>>
    fun observeAdviceRange(userId: String, fromDate: String, toDate: String, isRead: Boolean?): Flow<List<AdviceLog>>
    suspend fun generateAdvice(userId: String, statDate: String): AppResult<List<AdviceLog>>
    suspend fun markRead(adviceId: String, isRead: Boolean): AppResult<Unit>
}