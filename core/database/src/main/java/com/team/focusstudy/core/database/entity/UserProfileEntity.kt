package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: String,
    val nickname: String,
    @ColumnInfo(name = "avatar_uri")
    val avatarUri: String?,
    @ColumnInfo(name = "daily_target_minutes")
    val dailyTargetMinutes: Int,
    @ColumnInfo(name = "default_focus_minutes")
    val defaultFocusMinutes: Int,
    @ColumnInfo(name = "default_break_minutes")
    val defaultBreakMinutes: Int,
    val timezone: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)