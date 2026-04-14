package com.team.focusstudy.feature.advice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.AdviceRepository
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import com.team.focusstudy.core.data.usecase.GenerateAdviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdviceViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val adviceRepository: AdviceRepository,
    private val generateAdviceUseCase: GenerateAdviceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdviceUiState(statDate = LocalDate.now().toString()))
    val uiState: StateFlow<AdviceUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = ensureUserInitializedUseCase()
            val date = LocalDate.now().toString()
            _uiState.update { it.copy(userId = user.id, statDate = date) }
            adviceRepository.observeTodayAdvice(user.id, date).collect { list ->
                _uiState.update { it.copy(items = list, loading = false) }
            }
        }
    }

    fun generateToday() {
        val state = _uiState.value
        if (state.userId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, errorMessage = null) }
            when (val result = generateAdviceUseCase(state.userId, state.statDate)) {
                is AppResult.Success -> _uiState.update { it.copy(loading = false, errorMessage = null) }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        loading = false,
                        errorMessage = result.error.displayMessage
                    )
                }
            }
        }
    }

    fun markRead(adviceId: String, isRead: Boolean) {
        viewModelScope.launch {
            adviceRepository.markRead(adviceId, isRead)
        }
    }
}