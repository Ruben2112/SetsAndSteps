package com.heveamobile.mapbystep

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.heveamobile.mapbystep.theme.MapByStepTheme
import com.heveamobile.mapbystep.theme.darkScheme
import com.heveamobile.mapbystep.theme.lightScheme
import com.heveamobile.mapbystep.ui.home.HomeScreen

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

    MapByStepTheme {
        HomeScreen()
    }
}