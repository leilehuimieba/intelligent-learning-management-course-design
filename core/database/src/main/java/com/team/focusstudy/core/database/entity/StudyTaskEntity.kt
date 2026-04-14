package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.team.focusstudy.core.model.task.TaskStatus

@Entity(
    tableName = "study_task",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "status", "due_date"], name = "idx_task_user_status_due"),
        Index(value = ["user_id", "deleted_at"], name = "idx_task_user_deleted")
    ]
)
data class StudyTaskEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    val title: String,
    val description: String?,
    val category: String?,
    val priority: Int,
    val status: TaskStatus,
    @ColumnInfo(name = "due_date")
    val dueDate: Long?,
    @ColumnInfo(name = "estimated_minutes")
    val estimatedMinutes: Int,
    @ColumnInfo(name = "actual_minutes")
    val actualMinutes: Int,
    @ColumnInfo(name = "last_session_at")
    val lastSessionAt: Long?,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)