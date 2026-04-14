package com.team.focusstudy.core.model.task

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    ARCHIVED
}

data class StudyTask(
    val id: String,
    val userId: String,
    val title: String,
    val description: String?,
    val category: String?,
    val priority: Int,
    val status: TaskStatus,
    val dueDate: Long?,
    val estimatedMinutes: Int,
    val actualMinutes: Int,
    val lastSessionAt: Long?,
    val deletedAt: Long?,
    val createdAt: Long,
    val updatedAt: Long
)