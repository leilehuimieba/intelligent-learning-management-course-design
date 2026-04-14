package com.team.focusstudy.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

@Composable
fun HomeRoute(
    onOpenTask: () -> Unit,
    onOpenFocus: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAdvice: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onOpenTask = onOpenTask,
        onOpenFocus = onOpenFocus,
        onOpenStats = onOpenStats,
        onOpenAdvice = onOpenAdvice,
        onOpenSettings = onOpenSettings
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onOpenTask: () -> Unit,
    onOpenFocus: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAdvice: () -> Unit,
    onOpenSettings: () -> Unit
) {
    if (state.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator()
                Text(text = "正在加载学习概览...")
            }
        }
        return
    }

    val progress = if (state.dailyTargetMinutes <= 0) {
        0f
    } else {
        (state.todayFocusMinutes.toFloat() / state.dailyTargetMinutes.toFloat()).coerceIn(0f, 1f)
    }
    val progressPercent = (progress * 100).roundToInt()
    val remainMinutes = (state.dailyTargetMinutes - state.todayFocusMinutes).coerceAtLeast(0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "你好，${state.nickname}", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (remainMinutes == 0) {
                    "今天目标已完成，继续保持节奏。"
                } else {
                    "再坚持 $remainMinutes 分钟，就能完成今日目标。"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "今日专注进度", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${state.todayFocusMinutes} / ${state.dailyTargetMinutes} 分钟",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "完成度 $progressPercent%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "专注评分",
                    value = "%.1f".format(state.todayScore)
                )
                HomeMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "已专注",
                    value = "${state.todayFocusMinutes} 分"
                )
                HomeMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "剩余目标",
                    value = "${remainMinutes} 分"
                )
            }
        }

        item {
            Text(text = "快捷入口", style = MaterialTheme.typography.titleLarge)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilledTonalButton(onClick = onOpenTask, modifier = Modifier.weight(1f)) {
                    Text("任务管理")
                }
                FilledTonalButton(onClick = onOpenStats, modifier = Modifier.weight(1f)) {
                    Text("统计分析")
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilledTonalButton(onClick = onOpenAdvice, modifier = Modifier.weight(1f)) {
                    Text("今日建议")
                }
                FilledTonalButton(onClick = onOpenSettings, modifier = Modifier.weight(1f)) {
                    Text("系统设置")
                }
            }
        }

        item {
            Button(onClick = onOpenFocus, modifier = Modifier.fillMaxWidth()) {
                Text("立即开始专注")
            }
        }
    }
}

@Composable
private fun HomeMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
