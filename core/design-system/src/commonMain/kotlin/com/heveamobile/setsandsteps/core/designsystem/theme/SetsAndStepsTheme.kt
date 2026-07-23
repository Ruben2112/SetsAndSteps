package com.heveamobile.setsandsteps.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun SetsAndStepsTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}