package com.team.focusstudy.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = SurfaceCard,
    secondary = SecondaryMint,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryMint
)

@Composable
fun FocusStudyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = FocusTypography,
        content = content
    )
}