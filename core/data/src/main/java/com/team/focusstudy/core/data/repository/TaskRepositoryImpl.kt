package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.error.AppError
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.common.util.TimeProvider
import com.team.focusstudy.core.data.datasource.newId
import com.team.focusstudy.core.data.mapper.asEntity
import com.team.focusstudy.core.data.mapper.asModel
import com.team.focusstudy.core.database.dao.StudyTaskDao
import com.team.focusstudy.core.model.task.StudyTask
import com.team.focusstudy.core.model.task.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val studyTaskDao: StudyTaskDao
) : TaskRepository {

    override fun observeTasks(userId: String): Flow<List<StudyTask>> {
        return studyTaskDao.observeTasks(userId).map { list -> list.map { it.asModel() } }
    }

    override fun observeTask(taskId: String): Flow<StudyTask?> {
        return studyTaskDao.observeTask(taskId).map { it?.asModel() }
    }

    override suspend fun createTask(
        userId: String,
        title: String,
        description: String?,
        category: String?,
        priority: Int,
        dueDate: Long?,
        estimatedMinutes: Int
    ): AppResult<StudyTask> {
        val error = validateTaskInput(title, priority, estimatedMinutes)
        if (error != null) return AppResult.Failure(error)

        val now = TimeProvider.nowEpochMillis()
        val task = StudyTask(
            id = newId("t"),
            userId = userId,
            title = title.trim(),
            description = description?.trim()?.ifBlank { null },
            category = category?.trim()?.ifBlank { null },
            priority = priority,
            status = TaskStatus.TODO,
            dueDate = dueDate,
            estimatedMinutes = estimatedMinutes,
            actualMinutes = 0,
            lastSessionAt = null,
            deletedAt = null,
            createdAt = now,
            updatedAt = now
        )
        studyTaskDao.insert(task.asEntity())
        return AppResult.Success(task)
    }

    override suspend fun updateTask(task: StudyTask): AppResult<StudyTask> {
        val error = validateTaskInput(task.title, task.priority, task.estimatedMinutes)
        if (error != null) return AppResult.Failure(error)

        val current = studyTaskDao.getTask(task.id)?.asModel()
            ?: return AppResult.Failure(AppError.NotFound("任务不存在"))

        val updated = task.copy(
            userId = current.userId,
            createdAt = current.createdAt,
            updatedAt = TimeProvider.nowEpochMillis()
        )
        studyTaskDao.update(updated.asEntity())
        return AppResult.Success(updated)
    }

    override suspend fun changeTaskStatus(taskId: String, status: TaskStatus): AppResult<Unit> {
        val current = studyTaskDao.getTask(taskId)?.asModel()
            ?: return AppResult.Failure(AppError.NotFound("任务不存在"))

        if (!isValidTransition(current.status, status)) {
            return AppResult.Failure(AppError.Conflict("任务状态流转非法"))
        }

        studyTaskDao.updateStatus(taskId, status, TimeProvider.nowEpochMillis())
        return AppResult.Success(Unit)
    }

    override suspend fun softDeleteTask(taskId: String): AppResult<Unit> {
        val current = studyTaskDao.getTask(taskId)
            ?: return AppResult.Failure(AppError.NotFound("任务不存在"))
        studyTaskDao.softDelete(current.id, TimeProvider.nowEpochMillis())
        return AppResult.Success(Unit)
    }

    private fun validateTaskInput(title: String, priority: Int, estimatedMinutes: Int): AppError.Validation? {
        if (title.isBlank()) return AppError.Validation("任务标题不能为空")
        if (priority !in 1..3) return AppError.Validation("优先级必须为 1-3")
        if (estimatedMinutes !in 5..600) return AppError.Validation("预估时长必须在 5-600 分钟")
        return null
    }

    private fun isValidTransition(from: TaskStatus, to: TaskStatus): Boolean {
        if (from == to) return true
        if (to == TaskStatus.ARCHIVED) return true
        return when (from) {
            TaskStatus.TODO -> to == TaskStatus.IN_PROGRESS
            TaskStatus.IN_PROGRESS -> to == TaskStatus.DONE
            TaskStatus.DONE -> false
            TaskStatus.ARCHIVED -> false
        }
    }
}