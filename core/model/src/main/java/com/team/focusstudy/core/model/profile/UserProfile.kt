package com.team.focusstudy.core.model.profile

data class UserProfile(
    val id: String,
    val nickname: String,
    val avatarUri: String?,
    val dailyTargetMinutes: Int,
    val defaultFocusMinutes: Int,
    val defaultBreakMinutes: Int,
    val timezone: String,
    val createdAt: Long,
    val updatedAt: Long
)