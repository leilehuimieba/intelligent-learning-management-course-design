package com.team.focusstudy.core.model.advice

enum class AdviceType {
    TIME_MANAGEMENT,
    DISTRACTION_CONTROL,
    TASK_SPLIT,
    MOTIVATION,
    RECOVERY
}

data class AdviceLog(
    val id: String,
    val userId: String,
    val statDate: String,
    val adviceType: AdviceType,
    val triggerRule: String,
    val content: String,
    val priority: Int,
    val isRead: Boolean,
    val createdAt: Long
)