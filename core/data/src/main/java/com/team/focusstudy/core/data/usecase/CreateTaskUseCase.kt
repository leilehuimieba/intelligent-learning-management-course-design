package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.TaskRepository
import com.team.focusstudy.core.model.task.StudyTask
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        userId: String,
        title: String,
        description: String?,
        category: String?,
        priority: Int,
        dueDate: Long?,
        estimatedMinutes: Int
    ): AppResult<StudyTask> {
        return taskRepository.createTask(
            userId = userId,
            title = title,
            description = description,
            category = category,
            priority = priority,
            dueDate = dueDate,
            estimatedMinutes = estimatedMinutes
        )
    }
}