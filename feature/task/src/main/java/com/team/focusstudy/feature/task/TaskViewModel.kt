package com.team.focusstudy.feature.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.focusstudy.core.common.result.AppResult
import com.team.focusstudy.core.data.repository.TaskRepository
import com.team.focusstudy.core.data.usecase.CreateTaskUseCase
import com.team.focusstudy.core.data.usecase.EnsureUserInitializedUseCase
import com.team.focusstudy.core.model.task.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val ensureUserInitializedUseCase: EnsureUserInitializedUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = ensureUserInitializedUseCase()
            _uiState.update { it.copy(userId = user.id) }
            taskRepository.observeTasks(user.id).collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
        }
    }

    fun updateTitle(value: String) {
        _uiState.update { it.copy(titleInput = value, errorMessage = null, infoMessage = null) }
    }

    fun updateCategory(value: String) {
        _uiState.update { it.copy(categoryInput = value, errorMessage = null, infoMessage = null) }
    }

    fun updateEstimatedMinutes(value: String) {
        _uiState.update { it.copy(estimatedMinutesInput = value, errorMessage = null, infoMessage = null) }
    }

    fun updatePriority(priority: Int) {
        if (priority !in 1..3) return
        _uiState.update { it.copy(priorityInput = priority, errorMessage = null, infoMessage = null) }
    }

    fun changeFilter(filter: TaskFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun clearHintMessage() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }

    fun createTask() {
        val state = _uiState.value
        if (state.isSaving) return

        if (state.userId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "用户信息未准备好，请稍后重试") }
            return
        }

        val title = state.titleInput.trim()
        if (title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "请输入任务标题") }
            return
        }

        val estimatedMinutes = state.estimatedMinutesInput.toIntOrNull()
        if (estimatedMinutes == null) {
            _uiState.update { it.copy(errorMessage = "请输入有效时长") }
            return
        }

        if (estimatedMinutes !in 5..600) {
            _uiState.update { it.copy(errorMessage = "预计时长需在 5-600 分钟") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, infoMessage = null) }
            when (
                val result = createTaskUseCase(
                    userId = state.userId,
                    title = title,
                    description = null,
                    category = state.categoryInput.trim().ifBlank { null },
                    priority = state.priorityInput,
                    dueDate = null,
                    estimatedMinutes = estimatedMinutes
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            titleInput = "",
                            categoryInput = "",
                            estimatedMinutesInput = "30",
                            priorityInput = 2,
                            errorMessage = null,
                            infoMessage = "任务已创建"
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = result.error.displayMessage,
                            infoMessage = null
                        )
                    }
                }
            }
        }
    }

    fun markTaskInProgress(taskId: String) {
        updateTaskStatus(taskId, TaskStatus.IN_PROGRESS)
    }

    fun markTaskDone(taskId: String) {
        updateTaskStatus(taskId, TaskStatus.DONE)
    }

    fun archiveTask(taskId: String) {
        viewModelScope.launch {
            when (val result = taskRepository.softDeleteTask(taskId)) {
                is AppResult.Success -> _uiState.update { it.copy(errorMessage = null, infoMessage = "任务已归档") }
                is AppResult.Failure -> _uiState.update { it.copy(errorMessage = result.error.displayMessage) }
            }
        }
    }

    private fun updateTaskStatus(taskId: String, status: TaskStatus) {
        viewModelScope.launch {
            val result = taskRepository.changeTaskStatus(taskId, status)
            if (result is AppResult.Failure) {
                _uiState.update { it.copy(errorMessage = result.error.displayMessage) }
            }
        }
    }
}
