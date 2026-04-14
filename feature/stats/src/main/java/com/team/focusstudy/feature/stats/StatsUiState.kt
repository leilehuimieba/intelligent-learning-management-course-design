package com.team.focusstudy.feature.stats

import com.team.focusstudy.core.model.stat.DailyStat

enum class StatsPeriod {
    DAY,
    WEEK,
    MONTH
}

data class StatsUiState(
    val userId: String = "",
    val selectedPeriod: StatsPeriod = StatsPeriod.DAY,
    val statDate: String = "",
    val dailyStat: DailyStat? = null,
    val trendStats: List<DailyStat> = emptyList(),
    val loading: Boolean = true,
    val errorMessage: String? = null
)