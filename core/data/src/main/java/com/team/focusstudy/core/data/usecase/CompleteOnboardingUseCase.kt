package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.model.profile.UserProfile
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(
        nickname: String,
        dailyTargetMinutes: Int,
        defaultFocusMinutes: Int,
        defaultBreakMinutes: Int
    ): AppResult<UserProfile> {
        val result = profileRepository.updateProfile(
            nickname = nickname,
            dailyTargetMinutes = dailyTargetMinutes,
            defaultFocusMinutes = defaultFocusMinutes,
            defaultBreakMinutes = defaultBreakMinutes
        )
        if (result is AppResult.Success) {
            profileRepository.markOnboardingDone(result.data.id)
        }
        return result
    }
}