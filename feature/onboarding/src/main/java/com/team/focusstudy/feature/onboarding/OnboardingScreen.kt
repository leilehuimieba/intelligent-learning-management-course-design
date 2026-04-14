package com.team.focusstudy.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun OnboardingRoute(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is OnboardingEvent.NavigateHome) {
                onComplete()
            }
        }
    }

    OnboardingScreen(
        state = state,
        onNicknameChanged = viewModel::updateNickname,
        onDailyTargetChanged = viewModel::updateDailyTarget,
        onFocusMinutesChanged = viewModel::updateFocusMinutes,
        onBreakMinutesChanged = viewModel::updateBreakMinutes,
        onSubmit = viewModel::submit
    )
}

@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onNicknameChanged: (String) -> Unit,
    onDailyTargetChanged: (String) -> Unit,
    onFocusMinutesChanged: (String) -> Unit,
    onBreakMinutesChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "欢迎使用 Focus Study", style = MaterialTheme.typography.headlineMedium)
        Text(text = "先完成首次配置，后续可在设置页修改。", style = MaterialTheme.typography.bodyMedium)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.nickname,
            onValueChange = onNicknameChanged,
            label = { Text("昵称") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.dailyTargetMinutes,
            onValueChange = onDailyTargetChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("每日目标(30-720)") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.defaultFocusMinutes,
            onValueChange = onFocusMinutesChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("默认专注时长(5-180)") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.defaultBreakMinutes,
            onValueChange = onBreakMinutesChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("默认休息时长(1-60)") }
        )

        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onSubmit,
            enabled = !state.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(4.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("完成并进入首页")
            }
        }
    }
}