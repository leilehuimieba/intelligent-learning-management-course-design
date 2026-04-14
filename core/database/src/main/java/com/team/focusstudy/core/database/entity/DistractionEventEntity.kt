package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.team.focusstudy.core.model.session.DistractionType

@Entity(
    tableName = "distraction_event",
    foreignKeys = [
        ForeignKey(
            entity = FocusSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["session_id", "event_time"], name = "idx_event_session_time")
    ]
)
data class DistractionEventEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "event_type")
    val eventType: DistractionType,
    @ColumnInfo(name = "event_time")
    val eventTime: Long,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Int,
    val detail: String?
)