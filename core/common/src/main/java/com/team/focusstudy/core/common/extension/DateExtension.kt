package com.team.focusstudy.core.common.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val statFormatter = DateTimeFormatter.ISO_LOCAL_DATE

fun LocalDate.toStatDateString(): String = format(statFormatter)

fun String.toLocalDateOrNull(): LocalDate? = runCatching {
    LocalDate.parse(this, statFormatter)
}.getOrNull()