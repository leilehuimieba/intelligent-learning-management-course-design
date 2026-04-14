package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.team.focusstudy.core.model.advice.AdviceType

@Entity(
    tableName = "advice_log",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "stat_date", "is_read"], name = "idx_advice_user_date_read")
    ]
)
data class AdviceLogEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "stat_date")
    val statDate: String,
    @ColumnInfo(name = "advice_type")
    val adviceType: AdviceType,
    @ColumnInfo(name = "trigger_rule")
    val triggerRule: String,
    val content: String,
    val priority: Int,
    @ColumnInfo(name = "is_read")
    val isRead: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
