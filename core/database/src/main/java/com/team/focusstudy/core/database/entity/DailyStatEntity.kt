package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "daily_stat",
    primaryKeys = ["user_id", "stat_date"],
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "stat_date"], name = "idx_daily_stat_user_date")
    ]
)
data class DailyStatEntity(
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "stat_date")
    val statDate: String,
    @ColumnInfo(name = "total_focus_minutes")
    val totalFocusMinutes: Int,
    @ColumnInfo(name = "total_sessions")
    val totalSessions: Int,
    @ColumnInfo(name = "completed_sessions")
    val completedSessions: Int,
    @ColumnInfo(name = "completed_tasks")
    val completedTasks: Int,
    @ColumnInfo(name = "planned_tasks")
    val plannedTasks: Int,
    @ColumnInfo(name = "interruption_count")
    val interruptionCount: Int,
    @ColumnInfo(name = "consistency_days")
    val consistencyDays: Int,
    @ColumnInfo(name = "reach_rate")
    val reachRate: Double,
    @ColumnInfo(name = "completion_rate")
    val completionRate: Double,
    @ColumnInfo(name = "low_interrupt_rate")
    val lowInterruptRate: Double,
    @ColumnInfo(name = "consistency_rate")
    val consistencyRate: Double,
    @ColumnInfo(name = "focus_score")
    val focusScore: Double,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
