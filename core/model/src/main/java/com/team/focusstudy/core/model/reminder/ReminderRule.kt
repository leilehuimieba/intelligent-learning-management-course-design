package com.team.focusstudy.core.model.reminder

enum class ReminderType {
    STUDY_START,
    TASK_DUE,
    DAILY_SUMMARY
}

data class ReminderRule(
    val id: String,
    val userId: String,
    val ruleType: ReminderType,
    val hour: Int,
    val minute: Int,
    val weekMask: Int,
    val enabled: Boolean,
    val relatedTaskId: String?,
    val createdAt: Long,
    val updatedAt: Long
)