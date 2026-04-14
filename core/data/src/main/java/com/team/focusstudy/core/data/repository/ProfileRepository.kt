package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.model.profile.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<UserProfile?>
    suspend fun getOrCreateProfile(): UserProfile
    suspend fun updateProfile(
        nickname: String,
        dailyTargetMinutes: Int,
        defaultFocusMinutes: Int,
        defaultBreakMinutes: Int
    ): AppResult<UserProfile>

    suspend fun markOnboardingDone(userId: String)
    suspend fun isOnboardingDone(): Boolean
    suspend fun currentUserId(): String?
}