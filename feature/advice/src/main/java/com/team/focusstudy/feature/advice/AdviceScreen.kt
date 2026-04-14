package com.team.focusstudy.feature.advice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun AdviceRoute(
    onBack: () -> Unit,
    viewModel: AdviceViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AdviceScreen(
        state = state,
        onGenerate = viewModel::generateToday,
        onToggleRead = viewModel::markRead,
        onBack = onBack
    )
}

@Composable
fun AdviceScreen(
    state: AdviceUiState,
    onGenerate: () -> Unit,
    onToggleRead: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "今日建议", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onGenerate) { Text("生成建议") }
            Button(onClick = onBack) { Text("返回") }
        }

        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.items, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = item.adviceType.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = item.content)
                        Text(text = "规则: ${item.triggerRule}")
                        Button(onClick = { onToggleRead(item.id, !item.isRead) }) {
                            Text(if (item.isRead) "标记未读" else "标记已读")
                        }
                    }
                }
            }
        }
    }
}