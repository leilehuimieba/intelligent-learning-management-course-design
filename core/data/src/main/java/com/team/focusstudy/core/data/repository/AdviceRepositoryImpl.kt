package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.error.AppError
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.common.util.TimeProvider
import com.team.focusstudy.core.data.datasource.newId
import com.team.focusstudy.core.data.mapper.asEntity
import com.team.focusstudy.core.data.mapper.asModel
import com.team.focusstudy.core.database.dao.AdviceLogDao
import com.team.focusstudy.core.database.dao.DailyStatDao
import com.team.focusstudy.core.database.dao.StudyTaskDao
import com.team.focusstudy.core.model.advice.AdviceLog
import com.team.focusstudy.core.model.advice.AdviceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdviceRepositoryImpl @Inject constructor(
    private val adviceLogDao: AdviceLogDao,
    private val dailyStatDao: DailyStatDao,
    private val studyTaskDao: StudyTaskDao,
    private val statsRepository: StatsRepository
) : AdviceRepository {

    override fun observeTodayAdvice(userId: String, statDate: String): Flow<List<AdviceLog>> {
        return adviceLogDao.observeByDate(userId, statDate).map { list -> list.map { it.asModel() } }
    }

    override fun observeAdviceRange(
        userId: String,
        fromDate: String,
        toDate: String,
        isRead: Boolean?
    ): Flow<List<AdviceLog>> {
        return adviceLogDao.observeByRange(userId, fromDate, toDate, isRead).map { list -> list.map { it.asModel() } }
    }

    override suspend fun generateAdvice(userId: String, statDate: String): AppResult<List<AdviceLog>> {
        statsRepository.recalculateDailyStat(userId, statDate)
        val today = dailyStatDao.getByDate(userId, statDate)
            ?: return AppResult.Failure(AppError.NotFound("当日统计尚未生成"))

        val now = TimeProvider.nowEpochMillis()
        val suggestions = mutableListOf<AdviceLog>()

        if (isReachRateLowForThreeDays(userId, statDate)) {
            suggestions += AdviceLog(
                id = newId("a"),
                userId = userId,
                statDate = statDate,
                adviceType = AdviceType.TIME_MANAGEMENT,
                triggerRule = "RULE_A_LOW_3D",
                content = "连续 3 天目标达成率偏低，建议将单次专注时长下调 20%，先保证完成率再逐步拉长。",
                priority = 1,
                isRead = false,
                createdAt = now
            )
        }

        if (today.interruptionCount > today.totalSessions) {
            suggestions += AdviceLog(
                id = newId("a"),
                userId = userId,
                statDate = statDate,
                adviceType = AdviceType.DISTRACTION_CONTROL,
                triggerRule = "RULE_INTERRUPT_HIGH",
                content = "今日中断次数高于会话数，建议下次专注前开启免打扰并关闭非必要通知源。",
                priority = 1,
                isRead = false,
                createdAt = now
            )
        }

        val splitTask = studyTaskDao.getPotentialSplitTasks(userId).firstOrNull()
        if (splitTask != null) {
            suggestions += AdviceLog(
                id = newId("a"),
                userId = userId,
                statDate = statDate,
                adviceType = AdviceType.TASK_SPLIT,
                triggerRule = "RULE_TASK_SPLIT",
                content = "任务《${splitTask.title}》建议拆分为 15-25 分钟子任务，降低启动阻力并提升完成概率。",
                priority = 2,
                isRead = false,
                createdAt = now
            )
        }

        if (isConsistencyBroken(userId, statDate, today.consistencyDays)) {
            suggestions += AdviceLog(
                id = newId("a"),
                userId = userId,
                statDate = statDate,
                adviceType = AdviceType.RECOVERY,
                triggerRule = "RULE_CONSISTENCY_BROKEN",
                content = "连续打卡出现中断，建议明天先完成一个 10 分钟恢复会话，快速重建节奏。",
                priority = 2,
                isRead = false,
                createdAt = now
            )
        }

        if (suggestions.isEmpty()) {
            suggestions += AdviceLog(
                id = newId("a"),
                userId = userId,
                statDate = statDate,
                adviceType = AdviceType.MOTIVATION,
                triggerRule = "RULE_DEFAULT_MOTIVATION",
                content = "今日表现稳定，明天可保持当前节奏并优先推进最高优先级任务。",
                priority = 3,
                isRead = false,
                createdAt = now
            )
        }

        val finalList = suggestions.distinctBy { it.adviceType }.take(3)
        adviceLogDao.deleteByDate(userId, statDate)
        adviceLogDao.upsertAll(finalList.map { it.asEntity() })

        return AppResult.Success(finalList)
    }

    override suspend fun markRead(adviceId: String, isRead: Boolean): AppResult<Unit> {
        adviceLogDao.updateRead(adviceId, isRead)
        return AppResult.Success(Unit)
    }

    private suspend fun isReachRateLowForThreeDays(userId: String, statDate: String): Boolean {
        val targetDate = LocalDate.parse(statDate)
        val from = targetDate.minusDays(2).toString()
        val stats = dailyStatDao.getRange(userId, from, statDate)
        if (stats.size < 3) return false
        return stats.all { it.reachRate < 0.6 }
    }

    private suspend fun isConsistencyBroken(userId: String, statDate: String, consistencyDays: Int): Boolean {
        if (consistencyDays > 0) return false
        val yesterday = LocalDate.parse(statDate).minusDays(1).toString()
        val previous = dailyStatDao.getByDate(userId, yesterday) ?: return false
        return previous.consistencyDays > 0
    }
}