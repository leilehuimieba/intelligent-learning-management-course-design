package com.team.focusstudy.feature.home

data class HomeUiState(
    val nickname: String = "",
    val dailyTargetMinutes: Int = 120,
    val todayFocusMinutes: Int = 0,
    val todayScore: Double = 0.0,
    val loading: Boolean = true
)