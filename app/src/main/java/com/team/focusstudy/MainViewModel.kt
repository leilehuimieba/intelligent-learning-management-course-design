package com.team.focusstudy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.data.repository.ProfileRepository
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import com.team.focusstudy.navigation.Destinations
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            ensureUserInitializedUseCase()
            val onboardingDone = profileRepository.isOnboardingDone()
            _uiState.update {
                it.copy(
                    loading = false,
                    startDestination = if (onboardingDone) Destinations.HOME else Destinations.ONBOARDING
                )
            }
        }
    }
}