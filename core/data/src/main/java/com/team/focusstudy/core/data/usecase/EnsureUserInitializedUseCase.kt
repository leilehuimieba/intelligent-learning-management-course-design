package com.team.focusstudy.core.data.usecase

import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.model.profile.UserProfile
import javax.inject.Inject

class EnsureUserInitializedUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): UserProfile = profileRepository.getOrCreateProfile()
}