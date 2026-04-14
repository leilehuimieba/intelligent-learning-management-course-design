package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.model.task.StudyTask
import com.team.focusstudy.core.model.task.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(userId: String): Flow<List<StudyTask>>
    fun observeTask(taskId: String): Flow<StudyTask?>

    suspend fun createTask(
        userId: String,
        title: String,
        description: String?,
        category: String?,
        priority: Int,
        dueDate: Long?,
        estimatedMinutes: Int
    ): AppResult<StudyTask>

    suspend fun updateTask(task: StudyTask): AppResult<StudyTask>
    suspend fun changeTaskStatus(taskId: String, status: TaskStatus): AppResult<Unit>
    suspend fun softDeleteTask(taskId: String): AppResult<Unit>
}