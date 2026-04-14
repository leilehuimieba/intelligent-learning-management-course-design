package com.team.focusstudy.feature.focus

import com.team.focusstudy.core.data.repository.ActiveSession
import com.team.focusstudy.core.model.session.FocusState

data class FocusUiState(
    val userId: String = "",
    val taskId: String? = null,
    val focusState: FocusState = FocusState.IDLE,
    val activeSession: ActiveSession? = null,
    val elapsedSeconds: Long = 0,
    val remainingSeconds: Long = 0,
    val pauseDurationMillis: Long = 0,
    val pauseStartedAtMillis: Long? = null,
    val pauseCount: Int = 0,
    val interruptCount: Int = 0,
    val errorMessage: String? = null
)