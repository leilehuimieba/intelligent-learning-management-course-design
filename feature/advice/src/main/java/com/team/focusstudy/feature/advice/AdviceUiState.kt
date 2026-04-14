package com.team.focusstudy.feature.advice

import com.team.focusstudy.core.model.advice.AdviceLog

data class AdviceUiState(
    val userId: String = "",
    val statDate: String = "",
    val items: List<AdviceLog> = emptyList(),
    val loading: Boolean = true,
    val errorMessage: String? = null
)