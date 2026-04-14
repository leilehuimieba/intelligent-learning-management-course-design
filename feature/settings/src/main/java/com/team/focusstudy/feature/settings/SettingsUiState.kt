package com.team.focusstudy.feature.settings

data class SettingsUiState(
    val nickname: String = "",
    val dailyTargetMinutes: String = "120",
    val defaultFocusMinutes: String = "25",
    val defaultBreakMinutes: String = "5",
    val saving: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)