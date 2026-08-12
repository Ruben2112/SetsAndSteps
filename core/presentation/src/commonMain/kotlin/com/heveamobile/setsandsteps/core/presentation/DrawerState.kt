package com.heveamobile.setsandsteps.core.presentation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalDrawerState = staticCompositionLocalOf<DrawerState> {
    error("No DrawerState provided")
}
