package com.team.focusstudy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.data.repository.StatsRepository
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val profileRepository: ProfileRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = ensureUserInitializedUseCase()
            val statDate = LocalDate.now().toString()
            combine(
                profileRepository.observeProfile(),
                statsRepository.observeDailyStat(profile.id, statDate)
            ) { p, dailyStat ->
                HomeUiState(
                    nickname = p?.nickname ?: "学习者",
                    dailyTargetMinutes = p?.dailyTargetMinutes ?: 120,
                    todayFocusMinutes = dailyStat?.totalFocusMinutes ?: 0,
                    todayScore = dailyStat?.focusScore ?: 0.0,
                    loading = false
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }
}