package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.team.focusstudy.core.model.reminder.ReminderType

@Entity(
    tableName = "reminder_rule",
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
            childColumns = ["related_task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "enabled", "rule_type"], name = "idx_reminder_user_enabled")
    ]
)
data class ReminderRuleEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "rule_type")
    val ruleType: ReminderType,
    val hour: Int,
    val minute: Int,
    @ColumnInfo(name = "week_mask")
    val weekMask: Int,
    val enabled: Boolean,
    @ColumnInfo(name = "related_task_id")
    val relatedTaskId: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
