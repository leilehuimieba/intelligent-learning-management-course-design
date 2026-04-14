package com.team.focusstudy.core.data.repository

import androidx.room.withTransaction
import com.team.focusstudy.core.common.error.AppError
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.common.util.TimeProvider
import com.team.focusstudy.core.data.datasource.PreferenceDataSource
import com.team.focusstudy.core.data.datasource.newId
import com.team.focusstudy.core.data.mapper.asEntity
import com.team.focusstudy.core.data.mapper.asModel
import com.team.focusstudy.core.database.FocusStudyDatabase
import com.team.focusstudy.core.database.dao.DistractionEventDao
import com.team.focusstudy.core.database.dao.FocusSessionDao
import com.team.focusstudy.core.database.dao.StudyTaskDao
import com.team.focusstudy.core.model.session.DistractionEvent
import com.team.focusstudy.core.model.session.DistractionType
import com.team.focusstudy.core.model.session.FocusSession
import com.team.focusstudy.core.model.session.SessionStatus
import com.team.focusstudy.core.model.task.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class FocusSessionRepositoryImpl @Inject constructor(
    private val database: FocusStudyDatabase,
    private val focusSessionDao: FocusSessionDao,
    private val distractionEventDao: DistractionEventDao,
    private val studyTaskDao: StudyTaskDao,
    private val statsRepository: StatsRepository,
    private val preferenceDataSource: PreferenceDataSource
) : FocusSessionRepository {

    override fun observeRecentSessions(userId: String): Flow<List<FocusSession>> {
        return focusSessionDao.observeRecentSessions(userId).map { sessions -> sessions.map { it.asModel() } }
    }

    override suspend fun getActiveSession(): ActiveSession? {
        return preferenceDataSource.getActiveSession()
    }

    override suspend fun startSession(
        userId: String,
        taskId: String?,
        plannedMinutes: Int,
        startTime: Long
    ): AppResult<ActiveSession> {
        if (userId.isBlank()) {
            return AppResult.Failure(AppError.Validation("用户信息无效，请重新进入页面"))
        }

        if (plannedMinutes !in 5..180) {
            return AppResult.Failure(AppError.Validation("计划专注时长需在 5-180 分钟"))
        }

        val existingActive = preferenceDataSource.getActiveSession()
        if (existingActive != null) {
            val existingSession = focusSessionDao.getSession(existingActive.sessionId)
            if (existingSession != null && existingSession.endTime == null) {
                return AppResult.Failure(AppError.Conflict("已有进行中的专注会话，请先结束当前会话"))
            }
            preferenceDataSource.saveActiveSession(null)
        }

        val taskValidationError = validateLinkedTask(userId = userId, taskId = taskId)
        if (taskValidationError != null) {
            return taskValidationError
        }

        val now = TimeProvider.nowEpochMillis()
        val activeSession = ActiveSession(
            sessionId = newId("s"),
            userId = userId,
            taskId = taskId,
            startTime = startTime,
            plannedMinutes = plannedMinutes,
            pauseDurationMillis = 0,
            pauseCount = 0,
            interruptCount = 0
        )

        val session = FocusSession(
            id = activeSession.sessionId,
            userId = userId,
            taskId = taskId,
            startTime = startTime,
            endTime = null,
            plannedMinutes = plannedMinutes,
            actualMinutes = 0,
            pauseCount = 0,
            interruptCount = 0,
            sessionStatus = SessionStatus.INTERRUPTED,
            summaryNote = null,
            source = "MANUAL",
            createdAt = now
        )

        database.withTransaction {
            focusSessionDao.insert(session.asEntity())
            if (taskId != null) {
                val task = studyTaskDao.getTask(taskId)
                if (task != null && task.deletedAt == null && task.status == TaskStatus.TODO) {
                    studyTaskDao.updateStatus(taskId, TaskStatus.IN_PROGRESS, now)
                }
            }
        }

        preferenceDataSource.saveActiveSession(activeSession)
        return AppResult.Success(activeSession)
    }

    override suspend fun updateActiveSession(activeSession: ActiveSession) {
        preferenceDataSource.saveActiveSession(activeSession)
    }

    override suspend fun recordDistraction(
        sessionId: String,
        eventType: DistractionType,
        detail: String?,
        eventTime: Long,
        durationSeconds: Int
    ): AppResult<DistractionEvent> {
        val session = focusSessionDao.getSession(sessionId)
            ?: return AppResult.Failure(AppError.NotFound("会话不存在"))

        val event = DistractionEvent(
            id = newId("d"),
            sessionId = sessionId,
            eventType = eventType,
            eventTime = eventTime,
            durationSeconds = max(durationSeconds, 0),
            detail = detail?.trim()?.ifBlank { null }
        )

        distractionEventDao.insert(event.asEntity())
        focusSessionDao.update(session.copy(interruptCount = session.interruptCount + 1))
        return AppResult.Success(event)
    }

    override suspend fun endSession(
        sessionId: String,
        endTime: Long,
        pauseDurationMillis: Long,
        pauseCount: Int,
        interruptCount: Int,
        sessionStatus: SessionStatus,
        summaryNote: String?
    ): AppResult<FocusSession> {
        val session = focusSessionDao.getSession(sessionId)
            ?: return AppResult.Failure(AppError.NotFound("会话不存在"))

        if (session.endTime != null) {
            return AppResult.Failure(AppError.Conflict("会话已结束，不可重复结束"))
        }

        val actualMillis = max(endTime - session.startTime - pauseDurationMillis, 0)
        val actualMinutes = (actualMillis / 60_000L).toInt()

        val updated = session.copy(
            endTime = endTime,
            actualMinutes = actualMinutes,
            pauseCount = max(pauseCount, session.pauseCount),
            interruptCount = max(interruptCount, session.interruptCount),
            sessionStatus = sessionStatus,
            summaryNote = summaryNote?.trim()?.ifBlank { null }
        )

        database.withTransaction {
            focusSessionDao.update(updated)
            val taskId = updated.taskId
            if (taskId != null) {
                studyTaskDao.addActualMinutes(taskId, actualMinutes, endTime)
            }
            preferenceDataSource.saveActiveSession(null)
        }

        val statDate = Instant.ofEpochMilli(updated.startTime)
            .atZone(ZoneId.of("Asia/Shanghai"))
            .toLocalDate()
            .toString()

        statsRepository.recalculateDailyStat(updated.userId, statDate)
        return AppResult.Success(updated.asModel())
    }

    private suspend fun validateLinkedTask(userId: String, taskId: String?): AppResult.Failure? {
        if (taskId == null) return null

        val task = studyTaskDao.getTask(taskId)
            ?: return AppResult.Failure(AppError.NotFound("关联任务不存在"))

        if (task.userId != userId) {
            return AppResult.Failure(AppError.Conflict("关联任务不属于当前用户"))
        }

        if (task.deletedAt != null || task.status == TaskStatus.ARCHIVED) {
            return AppResult.Failure(AppError.Conflict("关联任务已归档，无法开始专注"))
        }

        if (task.status == TaskStatus.DONE) {
            return AppResult.Failure(AppError.Conflict("关联任务已完成，无需开始专注"))
        }

        return null
    }
}
