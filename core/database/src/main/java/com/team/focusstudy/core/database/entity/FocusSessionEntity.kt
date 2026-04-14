package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.team.focusstudy.core.model.session.SessionStatus

@Entity(
    tableName = "focus_session",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StudyTaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["user_id", "start_time"], name = "idx_session_user_start"),
        Index(value = ["task_id", "start_time"], name = "idx_session_task_start")
    ]
)
data class FocusSessionEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "task_id")
    val taskId: String?,
    @ColumnInfo(name = "start_time")
    val startTime: Long,
    @ColumnInfo(name = "end_time")
    val endTime: Long?,
    @ColumnInfo(name = "planned_minutes")
    val plannedMinutes: Int,
    @ColumnInfo(name = "actual_minutes")
    val actualMinutes: Int,
    @ColumnInfo(name = "pause_count")
    val pauseCount: Int,
    @ColumnInfo(name = "interrupt_count")
    val interruptCount: Int,
    @ColumnInfo(name = "session_status")
    val sessionStatus: SessionStatus,
    @ColumnInfo(name = "summary_note")
    val summaryNote: String?,
    val source: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)