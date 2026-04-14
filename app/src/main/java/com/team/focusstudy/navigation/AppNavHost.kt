package com.team.focusstudy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.team.focusstudy.feature.advice.AdviceRoute
import com.team.focusstudy.feature.focus.FocusRoute
import com.team.focusstudy.feature.home.HomeRoute
import com.team.focusstudy.feature.onboarding.OnboardingRoute
import com.team.focusstudy.feature.settings.SettingsRoute
import com.team.focusstudy.feature.stats.StatsRoute
import com.team.focusstudy.feature.task.TaskRoute

@Composable
fun AppNavHost(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.ONBOARDING) {
            OnboardingRoute(
                onComplete = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.HOME) {
            HomeRoute(
                onOpenTask = { navController.navigate(Destinations.TASK_LIST) },
                onOpenFocus = { navController.navigate(Destinations.focusRoute(null)) },
                onOpenStats = { navController.navigate(Destinations.STATS) },
                onOpenAdvice = { navController.navigate(Destinations.ADVICE) },
                onOpenSettings = { navController.navigate(Destinations.SETTINGS) }
            )
        }

        composable(Destinations.TASK_LIST) {
            TaskRoute(
                onBack = { navController.popBackStack() },
                onStartFocusWithTask = { taskId ->
                    navController.navigate(Destinations.focusRoute(taskId))
                }
            )
        }

        composable(
            route = Destinations.FOCUS,
            arguments = listOf(navArgument(NavArgs.TASK_ID) {
                type = NavType.StringType
                defaultValue = "none"
            })
        ) {
            FocusRoute(onBack = { navController.popBackStack() })
        }

        composable(Destinations.STATS) {
            StatsRoute(onBack = { navController.popBackStack() })
        }

        composable(Destinations.ADVICE) {
            AdviceRoute(onBack = { navController.popBackStack() })
        }

        composable(Destinations.SETTINGS) {
            SettingsRoute(onBack = { navController.popBackStack() })
        }
    }
}