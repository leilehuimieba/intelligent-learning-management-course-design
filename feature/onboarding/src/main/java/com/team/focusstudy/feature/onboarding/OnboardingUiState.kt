package com.team.focusstudy.feature.onboarding

data class OnboardingUiState(
    val nickname: String = "",
    val dailyTargetMinutes: String = "120",
    val defaultFocusMinutes: String = "25",
    val defaultBreakMinutes: String = "5",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)