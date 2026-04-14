package com.team.focusstudy.core.common.error

sealed class AppError(
    val code: Int,
    open val displayMessage: String
) {
    data class Validation(override val displayMessage: String) : AppError(100400, displayMessage)
    data class NotFound(override val displayMessage: String) : AppError(100404, displayMessage)
    data class Conflict(override val displayMessage: String) : AppError(100409, displayMessage)
    data class Internal(override val displayMessage: String) : AppError(100500, displayMessage)
}