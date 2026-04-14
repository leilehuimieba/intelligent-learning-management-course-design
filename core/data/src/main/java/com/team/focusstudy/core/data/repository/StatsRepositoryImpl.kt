package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.error.AppError
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.common.util.TimeProvider
import com.team.focusstudy.core.data.mapper.asEntity
import com.team.focusstudy.core.data.mapper.asModel
import com.team.focusstudy.core.database.dao.DailyStatDao
import com.team.focusstudy.core.database.dao.DistractionEventDao
import com.team.focusstudy.core.database.dao.FocusSessionDao
import com.team.focusstudy.core.database.dao.StudyTaskDao
import com.team.focusstudy.core.database.dao.UserProfileDao
import com.team.focusstudy.core.model.session.SessionStatus
import com.team.focusstudy.core.model.stat.DailyStat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val dailyStatDao: DailyStatDao,
    private val focusSessionDao: FocusSessionDao,
    private val distractionEventDao: DistractionEventDao,
    private val studyTaskDao: StudyTaskDao,
    private val userProfileDao: UserProfileDao
) : StatsRepository {

    override fun observeDailyStat(userId: String, statDate: String): Flow<DailyStat?> {
        return dailyStatDao.observeByDate(userId, statDate).map { it?.asModel() }
    }

    override fun observeRange(userId: String, fromDate: String, toDate: String): Flow<List<DailyStat>> {
        return dailyStatDao.observeRange(userId, fromDate, toDate).map { list -> list.map { it.asModel() } }
    }

    override suspend fun recalculateDailyStat(userId: String, statDate: String): AppResult<DailyStat> {
        val profile = userProfileDao.getProfile()?.asModel()
            ?: return AppResult.Failure(AppError.NotFound("用户资料不存在"))

        val totalFocusMinutes = focusSessionDao.sumActualMinutesByDate(userId, statDate)
        val totalSessions = focusSessionDao.countSessionsByDate(userId, statDate)
        val completedSessions = focusSessionDao.countSessionsByDateAndStatus(userId, statDate, SessionStatus.COMPLETED)
        val interruptionCount = distractionEventDao.countByDate(userId, statDate)
        val plannedTasks = studyTaskDao.countPlannedTasksByDate(userId, statDate)
        val completedTasks = studyTaskDao.countCompletedTasksByDate(userId, statDate)

        val consistencyDays = calculateConsistencyDays(userId, statDate, totalFocusMinutes > 0)
        val score = ScoreCalculator.calculate(
            totalFocusMinutes = totalFocusMinutes,
            dailyTargetMinutes = profile.dailyTargetMinutes,
            completedTasks = completedTasks,
            plannedTasks = plannedTasks,
            interruptionCount = interruptionCount,
            totalSessions = totalSessions,
            consistencyDays = consistencyDays
        )

        val now = TimeProvider.nowEpochMillis()
        val current = dailyStatDao.getByDate(userId, statDate)
        val stat = DailyStat(
            userId = userId,
            statDate = statDate,
            totalFocusMinutes = totalFocusMinutes,
            totalSessions = totalSessions,
            completedSessions = completedSessions,
            completedTasks = completedTasks,
            plannedTasks = plannedTasks,
            interruptionCount = interruptionCount,
            consistencyDays = consistencyDays,
            reachRate = score.reachRate,
            completionRate = score.completionRate,
            lowInterruptRate = score.lowInterruptRate,
            consistencyRate = score.consistencyRate,
            focusScore = score.focusScore,
            createdAt = current?.createdAt ?: now,
            updatedAt = now
        )

        dailyStatDao.upsert(stat.asEntity())
        return AppResult.Success(stat)
    }

    private suspend fun calculateConsistencyDays(userId: String, statDate: String, hasFocusToday: Boolean): Int {
        if (!hasFocusToday) return 0

        var days = 1
        var cursor = LocalDate.parse(statDate).minusDays(1)

        while (true) {
            val previous = dailyStatDao.getByDate(userId, cursor.toString()) ?: break
            if (previous.totalFocusMinutes <= 0) break
            days += 1
            cursor = cursor.minusDays(1)
        }

        return days
    }
}
