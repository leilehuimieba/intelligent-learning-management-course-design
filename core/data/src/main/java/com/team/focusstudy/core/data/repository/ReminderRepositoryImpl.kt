package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.mapper.asEntity
import com.team.focusstudy.core.data.mapper.asModel
import com.team.focusstudy.core.database.dao.ReminderRuleDao
import com.team.focusstudy.core.model.reminder.ReminderRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderRuleDao: ReminderRuleDao
) : ReminderRepository {

    override fun observeRules(userId: String): Flow<List<ReminderRule>> {
        return reminderRuleDao.observeByUser(userId).map { list -> list.map { it.asModel() } }
    }

    override suspend fun upsertRule(rule: ReminderRule): AppResult<Unit> {
        reminderRuleDao.insert(rule.asEntity())
        return AppResult.Success(Unit)
    }

    override suspend fun deleteRule(ruleId: String): AppResult<Unit> {
        reminderRuleDao.delete(ruleId)
        return AppResult.Success(Unit)
    }
}