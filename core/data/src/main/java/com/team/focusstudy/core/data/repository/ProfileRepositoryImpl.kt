package com.team.focusstudy.core.data.repository

import com.team.focusstudy.core.common.error.AppError
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.common.util.TimeProvider
import com.team.focusstudy.core.data.datasource.PreferenceDataSource
import com.team.focusstudy.core.data.datasource.newId
import com.team.focusstudy.core.data.mapper.asEntity
import com.team.focusstudy.core.data.mapper.asModel
import com.team.focusstudy.core.database.dao.UserProfileDao
import com.team.focusstudy.core.model.profile.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val preferenceDataSource: PreferenceDataSource
) : ProfileRepository {

    override fun observeProfile(): Flow<UserProfile?> {
        return userProfileDao.observeProfile().map { it?.asModel() }
    }

    override suspend fun getOrCreateProfile(): UserProfile {
        val existing = userProfileDao.getProfile()?.asModel()
        if (existing != null) {
            if (preferenceDataSource.currentUserId().isNullOrBlank()) {
                preferenceDataSource.setCurrentUserId(existing.id)
            }
            return existing
        }

        val now = TimeProvider.nowEpochMillis()
        val created = UserProfile(
            id = newId("u"),
            nickname = "学习者",
            avatarUri = null,
            dailyTargetMinutes = 120,
            defaultFocusMinutes = 25,
            defaultBreakMinutes = 5,
            timezone = "Asia/Shanghai",
            createdAt = now,
            updatedAt = now
        )

        userProfileDao.upsert(created.asEntity())
        preferenceDataSource.setCurrentUserId(created.id)
        return created
    }

    override suspend fun updateProfile(
        nickname: String,
        dailyTargetMinutes: Int,
        defaultFocusMinutes: Int,
        defaultBreakMinutes: Int
    ): AppResult<UserProfile> {
        if (nickname.isBlank()) {
            return AppResult.Failure(AppError.Validation("昵称不能为空"))
        }
        if (dailyTargetMinutes !in 30..720) {
            return AppResult.Failure(AppError.Validation("每日目标需在 30-720 分钟"))
        }
        if (defaultFocusMinutes !in 5..180) {
            return AppResult.Failure(AppError.Validation("默认专注时长需在 5-180 分钟"))
        }
        if (defaultBreakMinutes !in 1..60) {
            return AppResult.Failure(AppError.Validation("默认休息时长需在 1-60 分钟"))
        }

        val current = getOrCreateProfile()
        val updated = current.copy(
            nickname = nickname.trim(),
            dailyTargetMinutes = dailyTargetMinutes,
            defaultFocusMinutes = defaultFocusMinutes,
            defaultBreakMinutes = defaultBreakMinutes,
            updatedAt = TimeProvider.nowEpochMillis()
        )
        userProfileDao.upsert(updated.asEntity())
        return AppResult.Success(updated)
    }

    override suspend fun markOnboardingDone(userId: String) {
        preferenceDataSource.setCurrentUserId(userId)
        preferenceDataSource.setOnboardingDone(true)
    }

    override suspend fun isOnboardingDone(): Boolean = preferenceDataSource.isOnboardingDone()

    override suspend fun currentUserId(): String? = preferenceDataSource.currentUserId()
}