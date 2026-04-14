package com.team.focusstudy.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onNicknameChanged = viewModel::updateNickname,
        onDailyChanged = viewModel::updateDailyTarget,
        onFocusChanged = viewModel::updateFocus,
        onBreakChanged = viewModel::updateBreak,
        onSave = viewModel::save,
        onBack = onBack
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onNicknameChanged: (String) -> Unit,
    onDailyChanged: (String) -> Unit,
    onFocusChanged: (String) -> Unit,
    onBreakChanged: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "设置", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.nickname,
            onValueChange = onNicknameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("昵称") }
        )

        OutlinedTextField(
            value = state.dailyTargetMinutes,
            onValueChange = onDailyChanged,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("每日目标(30-720)") }
        )

        OutlinedTextField(
            value = state.defaultFocusMinutes,
            onValueChange = onFocusChanged,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("默认专注时长(5-180)") }
        )

        OutlinedTextField(
            value = state.defaultBreakMinutes,
            onValueChange = onBreakChanged,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("默认休息时长(1-60)") }
        )

        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
            Text("保存设置")
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("返回")
        }

        if (state.message != null) {
            Text(text = state.message, color = MaterialTheme.colorScheme.primary)
        }

        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}