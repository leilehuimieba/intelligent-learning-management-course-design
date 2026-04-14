package com.team.focusstudy.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_setting")
data class AppSettingEntity(
    @PrimaryKey
    @ColumnInfo(name = "setting_key")
    val settingKey: String,
    @ColumnInfo(name = "setting_value")
    val settingValue: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)