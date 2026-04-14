package com.team.focusstudy.feature.stats

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
import com.team.focusstudy.core.ui.chart.TrendLineChart

@Composable
fun StatsRoute(
    onBack: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    StatsScreen(
        state = state,
        onChangePeriod = viewModel::loadByPeriod,
        onRecalculate = viewModel::recalculateToday,
        onBack = onBack
    )
}

@Composable
fun StatsScreen(
    state: StatsUiState,
    onChangePeriod: (StatsPeriod) -> Unit,
    onRecalculate: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "统计分析", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onChangePeriod(StatsPeriod.DAY) }) { Text("日") }
            Button(onClick = { onChangePeriod(StatsPeriod.WEEK) }) { Text("周") }
            Button(onClick = { onChangePeriod(StatsPeriod.MONTH) }) { Text("月") }
            Button(onClick = onRecalculate) { Text("重算") }
            Button(onClick = onBack) { Text("返回") }
        }

        val daily = state.dailyStat
        if (daily != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "日期: ${daily.statDate}")
                    Text(text = "专注时长: ${daily.totalFocusMinutes} 分钟")
                    Text(text = "会话数: ${daily.totalSessions}")
                    Text(text = "完成任务: ${daily.completedTasks}/${daily.plannedTasks}")
                    Text(text = "中断: ${daily.interruptionCount}")
                    Text(text = "评分: ${"%.1f".format(daily.focusScore)}")
                }
            }
        }

        TrendLineChart(values = state.trendStats.map { it.focusScore.toFloat() })

        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}