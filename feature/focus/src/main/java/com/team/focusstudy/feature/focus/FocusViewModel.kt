package com.team.focusstudy.feature.focus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.common.util.TimeProvider
import com.team.focusstudy.core.data.repository.ActiveSession
import com.team.focusstudy.core.data.repository.FocusSessionRepository
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import com.team.focusstudy.core.data.usecase.EndFocusSessionUseCase
import com.team.focusstudy.core.data.usecase.RecordDistractionUseCase
import com.team.focusstudy.core.data.usecase.StartFocusSessionUseCase
import com.team.focusstudy.core.model.session.DistractionType
import com.team.focusstudy.core.model.session.FocusState
import com.team.focusstudy.core.model.session.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val focusSessionRepository: FocusSessionRepository,
    private val startFocusSessionUseCase: StartFocusSessionUseCase,
    private val endFocusSessionUseCase: EndFocusSessionUseCase,
    private val recordDistractionUseCase: RecordDistractionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null

    init {
        viewModelScope.launch {
            val user = ensureUserInitializedUseCase()
            val argTaskId = savedStateHandle.get<String>("taskId")?.takeUnless { it == "none" }
            _uiState.update {
                it.copy(
                    userId = user.id,
                    taskId = argTaskId
                )
            }
            restoreActiveSessionIfNeeded()
        }
    }

    fun startSession(plannedMinutes: Int = 25) {
        val state = _uiState.value
        if (state.focusState == FocusState.RUNNING || state.focusState == FocusState.PAUSED) return

        viewModelScope.launch {
            when (
                val result = startFocusSessionUseCase(
                    userId = state.userId,
                    taskId = state.taskId,
                    plannedMinutes = plannedMinutes,
                    startTime = TimeProvider.nowEpochMillis()
                )
            ) {
                is AppResult.Success -> {
                    val active = result.data
                    _uiState.update {
                        it.copy(
                            activeSession = active,
                            focusState = FocusState.RUNNING,
                            elapsedSeconds = 0,
                            remainingSeconds = (active.plannedMinutes * 60).toLong(),
                            pauseDurationMillis = 0,
                            pauseStartedAtMillis = null,
                            pauseCount = 0,
                            interruptCount = 0,
                            errorMessage = null
                        )
                    }
                    startTicker(active)
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(errorMessage = result.error.displayMessage) }
                }
            }
        }
    }

    fun pauseSession() {
        val state = _uiState.value
        if (state.focusState != FocusState.RUNNING) return

        tickerJob?.cancel()
        val now = TimeProvider.nowEpochMillis()
        _uiState.update {
            it.copy(
                focusState = FocusState.PAUSED,
                pauseStartedAtMillis = now,
                pauseCount = it.pauseCount + 1
            )
        }
    }

    fun resumeSession() {
        val state = _uiState.value
        if (state.focusState != FocusState.PAUSED || state.activeSession == null) return

        val now = TimeProvider.nowEpochMillis()
        val addedPause = now - (state.pauseStartedAtMillis ?: now)
        val newPauseDuration = state.pauseDurationMillis + addedPause
        _uiState.update {
            it.copy(
                focusState = FocusState.RUNNING,
                pauseDurationMillis = newPauseDuration,
                pauseStartedAtMillis = null
            )
        }

        viewModelScope.launch {
            focusSessionRepository.updateActiveSession(
                state.activeSession.copy(
                    pauseDurationMillis = newPauseDuration,
                    pauseCount = state.pauseCount
                )
            )
        }
        startTicker(state.activeSession)
    }

    fun recordDistraction(type: DistractionType) {
        val state = _uiState.value
        val active = state.activeSession ?: return

        viewModelScope.launch {
            val now = TimeProvider.nowEpochMillis()
            recordDistractionUseCase(
                sessionId = active.sessionId,
                eventType = type,
                detail = null,
                eventTime = now,
                durationSeconds = 0
            )
            val newInterruptCount = state.interruptCount + 1
            _uiState.update { it.copy(interruptCount = newInterruptCount) }
            focusSessionRepository.updateActiveSession(active.copy(interruptCount = newInterruptCount))
        }
    }

    fun endSession(summary: String?, completed: Boolean) {
        val state = _uiState.value
        val active = state.activeSession ?: return

        val finalPauseDuration = if (state.focusState == FocusState.PAUSED) {
            state.pauseDurationMillis + (TimeProvider.nowEpochMillis() - (state.pauseStartedAtMillis ?: TimeProvider.nowEpochMillis()))
        } else {
            state.pauseDurationMillis
        }

        viewModelScope.launch {
            val status = if (completed) SessionStatus.COMPLETED else SessionStatus.CANCELLED
            when (
                val result = endFocusSessionUseCase(
                    sessionId = active.sessionId,
                    endTime = TimeProvider.nowEpochMillis(),
                    pauseDurationMillis = finalPauseDuration,
                    pauseCount = state.pauseCount,
                    interruptCount = state.interruptCount,
                    sessionStatus = status,
                    summaryNote = summary
                )
            ) {
                is AppResult.Success -> {
                    tickerJob?.cancel()
                    _uiState.update {
                        it.copy(
                            focusState = FocusState.FINISHED,
                            activeSession = null,
                            remainingSeconds = 0,
                            pauseStartedAtMillis = null,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(errorMessage = result.error.displayMessage) }
                }
            }
        }
    }

    private suspend fun restoreActiveSessionIfNeeded() {
        val active = focusSessionRepository.getActiveSession() ?: return
        val now = TimeProvider.nowEpochMillis()
        val elapsedMillis = (now - active.startTime - active.pauseDurationMillis).coerceAtLeast(0)
        val elapsedSeconds = elapsedMillis / 1000
        val totalSeconds = (active.plannedMinutes * 60).toLong()
        val remaining = (totalSeconds - elapsedSeconds).coerceAtLeast(0)

        _uiState.update {
            it.copy(
                userId = active.userId,
                taskId = active.taskId,
                activeSession = active,
                focusState = if (remaining == 0L) FocusState.FINISHED else FocusState.RUNNING,
                elapsedSeconds = elapsedSeconds,
                remainingSeconds = remaining,
                pauseDurationMillis = active.pauseDurationMillis,
                pauseCount = active.pauseCount,
                interruptCount = active.interruptCount
            )
        }

        if (remaining == 0L) {
            endSession(summary = "自动结束", completed = true)
        } else {
            startTicker(active)
        }
    }

    private fun startTicker(activeSession: ActiveSession) {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                val state = _uiState.value
                if (state.focusState != FocusState.RUNNING) break

                val now = TimeProvider.nowEpochMillis()
                val elapsedMillis = (now - activeSession.startTime - state.pauseDurationMillis).coerceAtLeast(0)
                val elapsedSeconds = elapsedMillis / 1000
                val totalSeconds = (activeSession.plannedMinutes * 60).toLong()
                val remaining = (totalSeconds - elapsedSeconds).coerceAtLeast(0)

                _uiState.update {
                    it.copy(
                        elapsedSeconds = elapsedSeconds,
                        remainingSeconds = remaining
                    )
                }

                if (remaining <= 0L) {
                    endSession(summary = "自动完成", completed = true)
                    break
                }

                delay(1_000)
            }
        }
    }

    override fun onCleared() {
        tickerJob?.cancel()
        super.onCleared()
    }
}
