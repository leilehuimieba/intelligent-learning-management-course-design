package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.UserProfileEntity
import com.team.focusstudy.core.model.profile.UserProfile

fun UserProfileEntity.asModel(): UserProfile {
    return UserProfile(
        id = id,
        nickname = nickname,
        avatarUri = avatarUri,
        dailyTargetMinutes = dailyTargetMinutes,
        defaultFocusMinutes = defaultFocusMinutes,
        defaultBreakMinutes = defaultBreakMinutes,
        timezone = timezone,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserProfile.asEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        nickname = nickname,
        avatarUri = avatarUri,
        dailyTargetMinutes = dailyTargetMinutes,
        defaultFocusMinutes = defaultFocusMinutes,
        defaultBreakMinutes = defaultBreakMinutes,
        timezone = timezone,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}