package com.heveamobile.setsandsteps.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapboxMap(
    modifier: Modifier = Modifier,
    boundingBox: List<Double>,
)