package com.team.focusstudy.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.usecase.CompleteOnboardingUseCase
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingEvent>()
    val events: SharedFlow<OnboardingEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            val profile = ensureUserInitializedUseCase()
            _uiState.update {
                it.copy(
                    nickname = profile.nickname,
                    dailyTargetMinutes = profile.dailyTargetMinutes.toString(),
                    defaultFocusMinutes = profile.defaultFocusMinutes.toString(),
                    defaultBreakMinutes = profile.defaultBreakMinutes.toString()
                )
            }
        }
    }

    fun updateNickname(value: String) {
        _uiState.update { it.copy(nickname = value, errorMessage = null) }
    }

    fun updateDailyTarget(value: String) {
        _uiState.update { it.copy(dailyTargetMinutes = value, errorMessage = null) }
    }

    fun updateFocusMinutes(value: String) {
        _uiState.update { it.copy(defaultFocusMinutes = value, errorMessage = null) }
    }

    fun updateBreakMinutes(value: String) {
        _uiState.update { it.copy(defaultBreakMinutes = value, errorMessage = null) }
    }

    fun submit() {
        val state = _uiState.value
        val dailyTarget = state.dailyTargetMinutes.toIntOrNull()
        val focusMinutes = state.defaultFocusMinutes.toIntOrNull()
        val breakMinutes = state.defaultBreakMinutes.toIntOrNull()

        if (dailyTarget == null || focusMinutes == null || breakMinutes == null) {
            _uiState.update { it.copy(errorMessage = "请输入有效数字") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            when (
                val result = completeOnboardingUseCase(
                    nickname = state.nickname,
                    dailyTargetMinutes = dailyTarget,
                    defaultFocusMinutes = focusMinutes,
                    defaultBreakMinutes = breakMinutes
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    _events.emit(OnboardingEvent.NavigateHome)
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = result.error.displayMessage
                        )
                    }
                }
            }
        }
    }
}

sealed interface OnboardingEvent {
    data object NavigateHome : OnboardingEvent
}