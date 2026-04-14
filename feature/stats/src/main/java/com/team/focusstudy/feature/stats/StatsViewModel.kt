package com.team.focusstudy.feature.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.StatsRepository
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import com.team.focusstudy.core.data.usecase.RecalculateDailyStatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val statsRepository: StatsRepository,
    private val recalculateDailyStatUseCase: RecalculateDailyStatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState(statDate = LocalDate.now().toString()))
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        viewModelScope.launch {
            val user = ensureUserInitializedUseCase()
            _uiState.update { it.copy(userId = user.id) }
            loadByPeriod(StatsPeriod.DAY)
        }
    }

    fun loadByPeriod(period: StatsPeriod) {
        val userId = _uiState.value.userId
        if (userId.isBlank()) return

        _uiState.update { it.copy(selectedPeriod = period, loading = true, errorMessage = null) }

        val today = LocalDate.now()
        val (from, to) = when (period) {
            StatsPeriod.DAY -> today.toString() to today.toString()
            StatsPeriod.WEEK -> today.minusDays(6).toString() to today.toString()
            StatsPeriod.MONTH -> today.minusDays(29).toString() to today.toString()
        }

        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            statsRepository.observeRange(userId, from, to).collect { list ->
                val sorted = list.sortedBy { it.statDate }
                val daily = sorted.lastOrNull()
                _uiState.update {
                    it.copy(
                        trendStats = sorted,
                        dailyStat = daily,
                        loading = false
                    )
                }
            }
        }
    }

    fun recalculateToday() {
        val state = _uiState.value
        if (state.userId.isBlank()) return

        viewModelScope.launch {
            when (val result = recalculateDailyStatUseCase(state.userId, LocalDate.now().toString())) {
                is AppResult.Success -> _uiState.update { it.copy(errorMessage = null) }
                is AppResult.Failure -> _uiState.update { it.copy(errorMessage = result.error.displayMessage) }
            }
        }
    }
}