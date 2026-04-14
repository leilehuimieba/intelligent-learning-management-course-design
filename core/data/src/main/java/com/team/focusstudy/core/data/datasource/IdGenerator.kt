package com.team.focusstudy.core.data.datasource

import java.util.UUID

fun newId(prefix: String): String = "${prefix}_${UUID.randomUUID()}"