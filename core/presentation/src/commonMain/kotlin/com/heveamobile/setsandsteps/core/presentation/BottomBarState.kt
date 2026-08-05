package com.heveamobile.setsandsteps.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf

class BottomBarState {
    val content = mutableStateOf<(@Composable () -> Unit)?>(null)
}

val LocalBottomBarState = staticCompositionLocalOf<BottomBarState> {
    error("No BottomBarState provided")
}
