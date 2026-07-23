package com.heveamobile.setsandsteps

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.heveamobile.setsandsteps.theme.SetsAndStepsTheme
import com.heveamobile.setsandsteps.theme.darkScheme
import com.heveamobile.setsandsteps.theme.lightScheme
import com.heveamobile.setsandsteps.ui.home.HomeScreen

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    val darkTheme = isSystemInDarkTheme()
    when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    SetsAndStepsTheme {
        HomeScreen()
    }
}