package com.team.focusstudy.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

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
        _uiState.update { it.copy(nickname = value, message = null, errorMessage = null) }
    }

    fun updateDailyTarget(value: String) {
        _uiState.update { it.copy(dailyTargetMinutes = value, message = null, errorMessage = null) }
    }

    fun updateFocus(value: String) {
        _uiState.update { it.copy(defaultFocusMinutes = value, message = null, errorMessage = null) }
    }

    fun updateBreak(value: String) {
        _uiState.update { it.copy(defaultBreakMinutes = value, message = null, errorMessage = null) }
    }

    fun save() {
        val state = _uiState.value
        val daily = state.dailyTargetMinutes.toIntOrNull()
        val focus = state.defaultFocusMinutes.toIntOrNull()
        val rest = state.defaultBreakMinutes.toIntOrNull()

        if (daily == null || focus == null || rest == null) {
            _uiState.update { it.copy(errorMessage = "请输入有效数字") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(saving = true, message = null, errorMessage = null) }
            when (
                val result = profileRepository.updateProfile(
                    nickname = state.nickname,
                    dailyTargetMinutes = daily,
                    defaultFocusMinutes = focus,
                    defaultBreakMinutes = rest
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            saving = false,
                            message = "设置已保存"
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            saving = false,
                            errorMessage = result.error.displayMessage
                        )
                    }
                }
            }
        }
    }
}