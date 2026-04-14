package com.team.focusstudy.core.common.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object TimeProvider {
    fun nowEpochMillis(): Long = System.currentTimeMillis()

    fun epochMillisToLocalDate(epochMillis: Long, zoneId: ZoneId): LocalDate {
        return Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
    }
}