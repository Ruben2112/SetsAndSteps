package com.heveamobile.setsandsteps

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.svg.SvgDecoder
import com.heveamobile.setsandsteps.core.designsystem.theme.Typography
import com.heveamobile.setsandsteps.shell.HomeScreen
import dev.zwander.compose.DynamicMaterialTheme

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }

    DynamicMaterialTheme {
        MaterialTheme(typography = Typography) {
            HomeScreen()
        }
    }
}
