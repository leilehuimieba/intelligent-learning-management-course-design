package com.team.focusstudy.navigation

object Destinations {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val TASK_LIST = "task/list"
    const val TASK_EDIT = "task/edit/{taskId}"
    const val FOCUS = "focus/{taskId}"
    const val STATS = "stats"
    const val ADVICE = "advice"
    const val SETTINGS = "settings"

    fun focusRoute(taskId: String?): String = "focus/${taskId ?: "none"}"
}