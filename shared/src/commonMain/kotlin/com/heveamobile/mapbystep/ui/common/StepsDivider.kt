package com.heveamobile.mapbystep.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.ic_footstep
import mapbystep.shared.generated.resources.steps_divider_icon_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class StepPosition {
    Top,
    Bottom
}

@Composable
fun StepsDivider(
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    iconSize: Dp = 24.dp,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val spacingPx = spacing.roundToPx()

        // Measure one instance to see how wide it is
        val itemPlaceables = subcompose(
            "measure_item",
            {
                Step(
                    stepPosition = StepPosition.Top,
                    iconSize = iconSize,
                )
            },
        ).map {
            it.measure(constraints.copy(minWidth = 0))
        }

        if (itemPlaceables.isEmpty()) {
            return@SubcomposeLayout layout(
                0,
                0,
            ) {}
        }

        val itemWidth = itemPlaceables.first().width
        val itemHeight = itemPlaceables.first().height
        val maxWidth = constraints.maxWidth

        // Calculate how many items fit (item + spacing)
        // Logic: itemWidth * n + spacingPx * (n - 1) <= maxWidth
        val count = if (itemWidth + spacingPx > 0) {
            (maxWidth + spacingPx) / (itemWidth + spacingPx)
        } else {
            0
        }

        // Subcompose the actual number of items we need
        val finalPlaceables = (0 until count).flatMap { index ->
            subcompose(
                "item_$index",
                {
                    Step(
                        stepPosition = if ((index % 2) == 0) StepPosition.Top else StepPosition.Bottom,
                        iconSize = iconSize,
                    )
                },
            ).map {
                it.measure(constraints.copy(minWidth = 0))
            }
        }

        val totalWidth = if (count > 0) {
            (count * itemWidth) + ((count - 1) * spacingPx)
        } else {
            0
        }

        layout(
            totalWidth,
            itemHeight,
        ) {
            var xPosition = 0
            finalPlaceables.forEach { placeable ->
                placeable.placeRelative(
                    x = xPosition,
                    y = 0,
                )
                xPosition += itemWidth + spacingPx
            }
        }
    }
}

@Composable
fun Step(
    modifier: Modifier = Modifier,
    stepPosition: StepPosition,
    iconSize: Dp = 24.dp,
) {
    Column(modifier = modifier) {
        Icon(
            modifier = Modifier
                .rotate(90F)
                .size(iconSize),
            painter = painterResource(Res.drawable.ic_footstep),
            contentDescription = stringResource(Res.string.steps_divider_icon_description),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (stepPosition == StepPosition.Top) 1F else 0F),
        )
        Icon(
            modifier = Modifier
                .rotate(90F)
                .size(iconSize),
            painter = painterResource(Res.drawable.ic_footstep),
            contentDescription = stringResource(Res.string.steps_divider_icon_description),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (stepPosition == StepPosition.Bottom) 1F else 0F),
        )
    }
}