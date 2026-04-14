package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.model.reminder.ReminderRule
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun observeRules(userId: String): Flow<List<ReminderRule>>
    suspend fun upsertRule(rule: ReminderRule): AppResult<Unit>
    suspend fun deleteRule(ruleId: String): AppResult<Unit>
}