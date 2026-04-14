package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.ReminderRuleEntity
import com.team.focusstudy.core.model.reminder.ReminderRule

fun ReminderRuleEntity.asModel(): ReminderRule {
    return ReminderRule(
        id = id,
        userId = userId,
        ruleType = ruleType,
        hour = hour,
        minute = minute,
        weekMask = weekMask,
        enabled = enabled,
        relatedTaskId = relatedTaskId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ReminderRule.asEntity(): ReminderRuleEntity {
    return ReminderRuleEntity(
        id = id,
        userId = userId,
        ruleType = ruleType,
        hour = hour,
        minute = minute,
        weekMask = weekMask,
        enabled = enabled,
        relatedTaskId = relatedTaskId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}