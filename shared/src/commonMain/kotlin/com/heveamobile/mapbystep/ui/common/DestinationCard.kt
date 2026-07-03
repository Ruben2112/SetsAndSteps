package com.heveamobile.mapbystep.ui.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.heveamobile.mapbystep.FormatMode
import com.heveamobile.mapbystep.domain.model.Destination
import com.heveamobile.mapbystep.domain.model.Info
import com.heveamobile.mapbystep.formatAmount
import com.heveamobile.mapbystep.theme.color
import com.heveamobile.mapbystep.theme.spacing
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.destination_card_new
import mapbystep.shared.generated.resources.destination_card_unrevealed_card_title
import mapbystep.shared.generated.resources.ic_map_points
import mapbystep.shared.generated.resources.ic_question_mark
import mapbystep.shared.generated.resources.map_points_icon_description
import mapbystep.shared.generated.resources.new_icon_description
import mapbystep.shared.generated.resources.unrevealed_card_icon_description
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun DestinationCard(
    modifier: Modifier = Modifier,
    destination: Destination,
    isRevealed: Boolean,
    isNew: Boolean = false,
    raritySpoiler: Boolean = false,
    onClick: () -> Unit,
    isLarge: Boolean = false,
    mapPointsGained: Int? = null,
) {
    val rotation = animateFloatAsState(
        targetValue = if (isRevealed) 0F else 180F,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 16F * density
            }
            .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
            .clickable {
                onClick()
            },
    ) {
        Box(
            modifier = Modifier.graphicsLayer {
                alpha = if (rotation.value <= 90F) 1f else 0f
            },
        ) {
            CardFront(
                isLarge = isLarge,
                isNew = isNew,
                mapPointsGained = mapPointsGained,
                destination = destination,
            )
        }
        Box(
            modifier = Modifier.graphicsLayer {
                rotationY = 180F
                alpha = if (rotation.value > 90F) 1f else 0f
            },
        ) {
            CardBack(
                isLarge = isLarge,
                raritySpoiler = raritySpoiler,
                destination = destination,
            )
        }
    }
}

@OptIn(InternalResourceApi::class)
@Composable
private fun CardFront(
    modifier: Modifier = Modifier,
    isLarge: Boolean,
    isNew: Boolean = false,
    mapPointsGained: Int? = null,
    destination: Destination,
) {
    Box(
        modifier = modifier.aspectRatio(
            5F / 7F,
            matchHeightConstraintsFirst = true,
        ),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
                .border(
                    width = if (isLarge) 4.dp else 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium,
                )
                .background(if (destination.totalVisits > 0) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.primaryContainer),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1F),
            ) {
                if (destination.info is Info.CountryInfo) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val density = LocalDensity.current
                        val size = remember(
                            maxWidth,
                            maxHeight,
                        ) {
                            val maxMapboxDimension = 1280
                            IntSize(
                                width = with(density) { maxWidth.roundToPx() }.coerceAtMost(maxMapboxDimension),
                                height = with(density) { maxHeight.roundToPx() }.coerceAtMost(maxMapboxDimension),
                            )
                        }

                        MapboxImage(
                            modifier = Modifier.fillMaxSize(),
                            countryInfo = destination.info,
                            rarity = destination.rarity,
                            size = size,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isLarge) 4.dp else 1.dp)
                    .background(MaterialTheme.colorScheme.outline),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = if (isLarge) MaterialTheme.spacing.large else MaterialTheme.spacing.small,
                        vertical = MaterialTheme.spacing.small,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = destination.name,
                    textAlign = TextAlign.Center,
                    style = if (isLarge) MaterialTheme.typography.bodyLarge.copy(destination.rarity.color(MaterialTheme.colorScheme.onPrimary)) else MaterialTheme.typography.bodySmall.copy(destination.rarity.color(MaterialTheme.colorScheme.onPrimary)),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (isNew) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        if (isLarge) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
                    ),
                contentAlignment = Alignment.TopEnd,
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh) {
                    if (isLarge) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small,
                            ),
                            text = if (isLarge) stringResource(Res.string.destination_card_new) else "",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    } else {
                        Icon(
                            Icons.Default.Star,
                            modifier = Modifier.size(8.dp),
                            contentDescription = stringResource(Res.string.new_icon_description),
                            tint = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                }
            }
        }
        if (isLarge && mapPointsGained != null) {
            Box(
                modifier = Modifier
                    .aspectRatio(1F)
                    .padding(
                        if (isLarge) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
                    ),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(resource = Res.drawable.ic_map_points),
                            modifier = Modifier.size(20.dp),
                            contentDescription = stringResource(Res.string.map_points_icon_description),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = formatAmount(
                                mapPointsGained,
                                formatMode = FormatMode.Long,
                            ),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardBack(
    modifier: Modifier = Modifier,
    isLarge: Boolean,
    raritySpoiler: Boolean = false,
    destination: Destination,
) {
    Column(
        modifier = modifier
            .aspectRatio(
                5F / 7F,
                matchHeightConstraintsFirst = true,
            )
            .clip(if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium)
            .border(
                width = if (isLarge) 4.dp else 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = if (isLarge) MaterialTheme.shapes.large else MaterialTheme.shapes.medium,
            )
            .background(if (destination.totalVisits > 0) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F),
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(resource = Res.drawable.ic_question_mark),
                contentDescription = stringResource(Res.string.unrevealed_card_icon_description),
                tint = if (raritySpoiler) destination.rarity.color(MaterialTheme.colorScheme.onPrimary) else MaterialTheme.colorScheme.onSurface,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isLarge) 4.dp else 1.dp)
                .background(MaterialTheme.colorScheme.outline),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = if (isLarge) MaterialTheme.spacing.large else MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.small,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.destination_card_unrevealed_card_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            )
        }
    }
}