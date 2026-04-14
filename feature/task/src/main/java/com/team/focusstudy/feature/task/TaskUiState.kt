package com.team.focusstudy.feature.task

import com.team.focusstudy.core.model.task.StudyTask

enum class TaskFilter {
    ALL,
    TODO,
    IN_PROGRESS,
    DONE
}

data class TaskUiState(
    val userId: String = "",
    val tasks: List<StudyTask> = emptyList(),
    val titleInput: String = "",
    val categoryInput: String = "",
    val estimatedMinutesInput: String = "30",
    val priorityInput: Int = 2,
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val isSaving: Boolean = false
)
