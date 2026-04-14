package com.team.focusstudy.core.data.mapper

import com.team.focusstudy.core.database.entity.AdviceLogEntity
import com.team.focusstudy.core.model.advice.AdviceLog

fun AdviceLogEntity.asModel(): AdviceLog {
    return AdviceLog(
        id = id,
        userId = userId,
        statDate = statDate,
        adviceType = adviceType,
        triggerRule = triggerRule,
        content = content,
        priority = priority,
        isRead = isRead,
        createdAt = createdAt
    )
}

fun AdviceLog.asEntity(): AdviceLogEntity {
    return AdviceLogEntity(
        id = id,
        userId = userId,
        statDate = statDate,
        adviceType = adviceType,
        triggerRule = triggerRule,
        content = content,
        priority = priority,
        isRead = isRead,
        createdAt = createdAt
    )
}