package com.team.focusstudy.feature.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.focusstudy.core.model.task.StudyTask
import com.team.focusstudy.core.model.task.TaskStatus
import com.team.focusstudy.core.ui.component.EmptyStateCard

@Composable
fun TaskRoute(
    onBack: () -> Unit,
    onStartFocusWithTask: (String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TaskScreen(
        state = state,
        onBack = onBack,
        onTitleChanged = viewModel::updateTitle,
        onCategoryChanged = viewModel::updateCategory,
        onEstimatedChanged = viewModel::updateEstimatedMinutes,
        onPriorityChanged = viewModel::updatePriority,
        onCreateTask = viewModel::createTask,
        onInProgress = viewModel::markTaskInProgress,
        onDone = viewModel::markTaskDone,
        onArchive = viewModel::archiveTask,
        onFilterChanged = viewModel::changeFilter,
        onStartFocusWithTask = onStartFocusWithTask
    )
}

@Composable
fun TaskScreen(
    state: TaskUiState,
    onBack: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onEstimatedChanged: (String) -> Unit,
    onPriorityChanged: (Int) -> Unit,
    onCreateTask: () -> Unit,
    onInProgress: (String) -> Unit,
    onDone: (String) -> Unit,
    onArchive: (String) -> Unit,
    onFilterChanged: (TaskFilter) -> Unit,
    onStartFocusWithTask: (String) -> Unit
) {
    val visibleTasks = when (state.selectedFilter) {
        TaskFilter.ALL -> state.tasks
        TaskFilter.TODO -> state.tasks.filter { it.status == TaskStatus.TODO }
        TaskFilter.IN_PROGRESS -> state.tasks.filter { it.status == TaskStatus.IN_PROGRESS }
        TaskFilter.DONE -> state.tasks.filter { it.status == TaskStatus.DONE }
    }

    val todoCount = state.tasks.count { it.status == TaskStatus.TODO }
    val inProgressCount = state.tasks.count { it.status == TaskStatus.IN_PROGRESS }
    val doneCount = state.tasks.count { it.status == TaskStatus.DONE }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "任务管理", style = MaterialTheme.typography.headlineMedium)
            FilledTonalButton(onClick = onBack) {
                Text("返回")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "快速创建任务", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = state.titleInput,
                    onValueChange = onTitleChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("任务标题") },
                    placeholder = { Text("例如：完成高数作业第 3 章") }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.categoryInput,
                        onValueChange = onCategoryChanged,
                        modifier = Modifier.weight(1f),
                        label = { Text("分类") },
                        placeholder = { Text("课程/复习/项目") }
                    )
                    OutlinedTextField(
                        value = state.estimatedMinutesInput,
                        onValueChange = onEstimatedChanged,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("预估分钟") }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PriorityChip(
                        selected = state.priorityInput == 1,
                        text = "高优先级",
                        onClick = { onPriorityChanged(1) }
                    )
                    PriorityChip(
                        selected = state.priorityInput == 2,
                        text = "中优先级",
                        onClick = { onPriorityChanged(2) }
                    )
                    PriorityChip(
                        selected = state.priorityInput == 3,
                        text = "低优先级",
                        onClick = { onPriorityChanged(3) }
                    )
                }

                Button(
                    onClick = onCreateTask,
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.padding(2.dp), strokeWidth = 2.dp)
                    } else {
                        Text("创建任务")
                    }
                }
            }
        }

        if (state.infoMessage != null) {
            Text(text = state.infoMessage, color = MaterialTheme.colorScheme.primary)
        }
        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "全部 ${state.tasks.size}")
                Text(text = "待办 $todoCount")
                Text(text = "进行中 $inProgressCount")
                Text(text = "已完成 $doneCount")
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TaskFilterChip(
                selected = state.selectedFilter == TaskFilter.ALL,
                text = "全部",
                onClick = { onFilterChanged(TaskFilter.ALL) }
            )
            TaskFilterChip(
                selected = state.selectedFilter == TaskFilter.TODO,
                text = "待办",
                onClick = { onFilterChanged(TaskFilter.TODO) }
            )
            TaskFilterChip(
                selected = state.selectedFilter == TaskFilter.IN_PROGRESS,
                text = "进行中",
                onClick = { onFilterChanged(TaskFilter.IN_PROGRESS) }
            )
            TaskFilterChip(
                selected = state.selectedFilter == TaskFilter.DONE,
                text = "已完成",
                onClick = { onFilterChanged(TaskFilter.DONE) }
            )
        }

        if (visibleTasks.isEmpty()) {
            EmptyStateCard(
                title = "当前筛选下没有任务",
                description = "先创建一个任务，或者切换筛选查看其他状态。",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visibleTasks, key = { it.id }) { task ->
                    TaskItemCard(
                        task = task,
                        onInProgress = onInProgress,
                        onDone = onDone,
                        onArchive = onArchive,
                        onStartFocusWithTask = onStartFocusWithTask
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItemCard(
    task: StudyTask,
    onInProgress: (String) -> Unit,
    onDone: (String) -> Unit,
    onArchive: (String) -> Unit,
    onStartFocusWithTask: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = statusLabel(task.status),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "分类: ${task.category ?: "未分类"}  |  优先级: ${priorityLabel(task.priority)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "预计 ${task.estimatedMinutes} 分钟，已投入 ${task.actualMinutes} 分钟",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (task.status == TaskStatus.TODO) {
                    FilledTonalButton(onClick = { onInProgress(task.id) }) {
                        Text("设为进行中")
                    }
                }
                if (task.status == TaskStatus.IN_PROGRESS) {
                    FilledTonalButton(onClick = { onDone(task.id) }) {
                        Text("标记完成")
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onStartFocusWithTask(task.id) }) {
                    Text("进入专注")
                }
                OutlinedButton(onClick = { onArchive(task.id) }) {
                    Text("归档")
                }
            }
        }
    }
}

@Composable
private fun PriorityChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) }
    )
}

@Composable
private fun TaskFilterChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) }
    )
}

private fun statusLabel(status: TaskStatus): String {
    return when (status) {
        TaskStatus.TODO -> "待开始"
        TaskStatus.IN_PROGRESS -> "进行中"
        TaskStatus.DONE -> "已完成"
        TaskStatus.ARCHIVED -> "已归档"
    }
}

private fun priorityLabel(priority: Int): String {
    return when (priority) {
        1 -> "高"
        2 -> "中"
        3 -> "低"
        else -> "中"
    }
}
