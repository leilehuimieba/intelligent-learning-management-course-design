package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.AppSettingEntity
import com.team.focusstudy.core.model.profile.AppSetting

fun AppSettingEntity.asModel(): AppSetting {
    return AppSetting(
        key = settingKey,
        value = settingValue,
        updatedAt = updatedAt
    )
}

fun AppSetting.asEntity(): AppSettingEntity {
    return AppSettingEntity(
        settingKey = key,
        settingValue = value,
        updatedAt = updatedAt
    )
}