package com.heveamobile.setsandsteps.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapboxMap(
    boundingBox: List<Double>,
    modifier: Modifier = Modifier,
)