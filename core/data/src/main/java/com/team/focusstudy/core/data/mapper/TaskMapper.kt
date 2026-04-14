package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.StudyTaskEntity
import com.team.focusstudy.core.model.task.StudyTask

fun StudyTaskEntity.asModel(): StudyTask {
    return StudyTask(
        id = id,
        userId = userId,
        title = title,
        description = description,
        category = category,
        priority = priority,
        status = status,
        dueDate = dueDate,
        estimatedMinutes = estimatedMinutes,
        actualMinutes = actualMinutes,
        lastSessionAt = lastSessionAt,
        deletedAt = deletedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun StudyTask.asEntity(): StudyTaskEntity {
    return StudyTaskEntity(
        id = id,
        userId = userId,
        title = title,
        description = description,
        category = category,
        priority = priority,
        status = status,
        dueDate = dueDate,
        estimatedMinutes = estimatedMinutes,
        actualMinutes = actualMinutes,
        lastSessionAt = lastSessionAt,
        deletedAt = deletedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}