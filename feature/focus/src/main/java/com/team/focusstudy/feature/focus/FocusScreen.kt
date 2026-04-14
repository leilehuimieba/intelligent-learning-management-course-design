package com.team.focusstudy.feature.focus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.focusstudy.core.model.session.DistractionType
import com.team.focusstudy.core.model.session.FocusState

@Composable
fun FocusRoute(
    onBack: () -> Unit,
    viewModel: FocusViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FocusScreen(
        state = state,
        onStart = { viewModel.startSession(25) },
        onPause = viewModel::pauseSession,
        onResume = viewModel::resumeSession,
        onDistraction = viewModel::recordDistraction,
        onComplete = { viewModel.endSession(summary = "专注完成", completed = true) },
        onCancel = { viewModel.endSession(summary = "手动取消", completed = false) },
        onBack = onBack
    )
}

@Composable
fun FocusScreen(
    state: FocusUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onDistraction: (DistractionType) -> Unit,
    onComplete: () -> Unit,
    onCancel: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "专注会话", style = MaterialTheme.typography.headlineMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "状态: ${state.focusState}")
                Text(text = "剩余: ${formatSeconds(state.remainingSeconds)}")
                Text(text = "已专注: ${formatSeconds(state.elapsedSeconds)}")
                Text(text = "中断次数: ${state.interruptCount}")
                if (state.taskId != null) {
                    Text(text = "关联任务: ${state.taskId}")
                }
            }
        }

        when (state.focusState) {
            FocusState.IDLE, FocusState.FINISHED -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onStart) { Text("开始") }
                    Button(onClick = onBack) { Text("返回") }
                }
            }

            FocusState.RUNNING -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onPause) { Text("暂停") }
                    Button(onClick = onComplete) { Text("完成") }
                    Button(onClick = onCancel) { Text("取消") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onDistraction(DistractionType.NOTIFICATION) }) { Text("通知中断") }
                    Button(onClick = { onDistraction(DistractionType.APP_SWITCH) }) { Text("切屏中断") }
                }
            }

            FocusState.PAUSED -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onResume) { Text("继续") }
                    Button(onClick = onComplete) { Text("结束") }
                    Button(onClick = onCancel) { Text("取消") }
                }
            }
        }

        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

private fun formatSeconds(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}