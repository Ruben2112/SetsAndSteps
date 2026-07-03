package com.heveamobile.mapbystep.ui.home

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.heveamobile.mapbystep.FormatMode
import com.heveamobile.mapbystep.domain.model.Destination
import com.heveamobile.mapbystep.formatAmount
import com.heveamobile.mapbystep.theme.color
import com.heveamobile.mapbystep.theme.spacing
import com.heveamobile.mapbystep.ui.common.Card
import com.heveamobile.mapbystep.ui.common.CountryInfoCard
import com.heveamobile.mapbystep.ui.common.DestinationCard
import com.heveamobile.mapbystep.ui.common.PrimaryButton
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.close_screen_button
import mapbystep.shared.generated.resources.ic_map_points
import mapbystep.shared.generated.resources.map_points_icon_description
import mapbystep.shared.generated.resources.overlay_destinations_visited
import mapbystep.shared.generated.resources.overlay_map_points_gained
import mapbystep.shared.generated.resources.overlay_new_destinations
import mapbystep.shared.generated.resources.overlay_reveal_all_button
import mapbystep.shared.generated.resources.overlay_reveal_button
import mapbystep.shared.generated.resources.overlay_skip_button
import mapbystep.shared.generated.resources.overlay_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun VisitedDestinationsOverlay(
    modifier: Modifier = Modifier,
    state: VisitedDestinationsState,
    onAction: (HomeAction) -> Unit,
) {
    val destinations = state.destinations
    val revealedDestinations = state.destinations.filter { it.isRevealed }

    val highestRarityDestination = revealedDestinations.maxByOrNull { it.rarity }
    val highestRarityColor = if (highestRarityDestination?.isRevealed == true) {
        highestRarityDestination.rarity
            .color(MaterialTheme.colorScheme.onPrimary)
            .copy(alpha = 0.25F)
    } else {
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.75F)
    }

    val backgroundColor = remember { Animatable(highestRarityColor) }

    LaunchedEffect(
        highestRarityDestination,
    ) {
        backgroundColor.animateTo(
            highestRarityColor,
            tween(300),
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor.value)
            .pointerInput(Unit) {
                detectTapGestures {
                    /* Consumes tap events so they don't reach the UI behind */
                }
            },
    ) {
        AnimatedContent(
            modifier = Modifier.fillMaxHeight(),
            targetState = state.destinationShown,
            transitionSpec = {
                // Smooth fade when opening/closing the detail view
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
        ) { destinationShown ->
            if (destinationShown != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CountryInfoCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.large),
                        destination = destinationShown,
                    )
                }
            } else {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val density = LocalDensity.current
                    val height = with(density) { maxHeight.roundToPx() }
                    val width = with(density) { maxWidth.roundToPx() }

                    GridLayout(
                        state = state,
                        destinations = destinations,
                        onAction = onAction,
                        showResultSummary = state.showResultSummary,
                        mapPointsGained = state.mapPointsGained,
                        screenHeight = height,
                        screenWidth = width,
                    )
                }
            }
        }

        val showRevealAllButton = destinations.any { !it.isRevealed }

        AnimatedVisibility(
            visible = !showRevealAllButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = MaterialTheme.spacing.large,
                    ),
                contentAlignment = Alignment.BottomCenter,
            ) {
                FloatingActionButton(
                    onClick = {
                        if (state.destinationShown != null) {
                            onAction(HomeAction.ToggleDestinationInfo(null))
                        } else {
                            onAction(HomeAction.CloseVisitedDestinations)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(Res.string.close_screen_button),
                        modifier = Modifier.size(MaterialTheme.spacing.large),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showRevealAllButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = MaterialTheme.spacing.large,
                    ),
                contentAlignment = Alignment.BottomCenter,
            ) {
                val buttonTextResId = if (!state.isRevealingAll) {
                    if (destinations.size > 1) Res.string.overlay_reveal_all_button else Res.string.overlay_reveal_button
                } else {
                    Res.string.overlay_skip_button
                }
                PrimaryButton(
                    label = stringResource(buttonTextResId),
                    onClick = {
                        if (!state.isRevealingAll) {
                            onAction(HomeAction.RevealAllDestinations)
                        } else {
                            onAction(HomeAction.SkipRevealingAllDestinations)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun GridLayout(
    state: VisitedDestinationsState,
    destinations: List<Destination>,
    onAction: (HomeAction) -> Unit,
    showResultSummary: Boolean,
    mapPointsGained: Int,
    screenHeight: Int,
    screenWidth: Int,
) {
    val gridState = rememberLazyGridState()

    val density = LocalDensity.current
    val smallSpacingPx = with(density) { MaterialTheme.spacing.small.toPx() }
    val mediumSpacingPx = with(density) { MaterialTheme.spacing.medium.toPx() }
    val largeSpacingPx = with(density) { MaterialTheme.spacing.large.toPx() }

    // Auto scroll logic for revealing all destinations
    var wasSummaryShown by remember { mutableStateOf(state.showResultSummary) }
    LaunchedEffect(
        destinations.filter { it.isRevealed },
        state.isRevealingAll,
        state.showResultSummary,
    ) {
        if (state.isRevealingAll) {
            val lastRevealedIndex = destinations.indexOfLast { it.isRevealed }

            if (lastRevealedIndex != -1) {
                // Calculate size of the cards to determine how far we have to scroll
                val horizontalPadding = mediumSpacingPx * 2
                val gridContentWidth = screenWidth - horizontalPadding

                val itemWidth = (gridContentWidth - (smallSpacingPx * 2)) / 3
                val itemHeight = itemWidth * (7F / 5F)

                /*
                   Target Offset Logic:
                   By default, index 0 is at the top (y=0).
                   To put the item at the bottom:
                   Offset = ScreenHeight - ItemHeight - BottomSafetyMargin
                   We make it NEGATIVE because scrollOffset moves the 'viewport' start point.
                */

                // Prevent it being hidden behind close button
                val bottomSafetyMargin = largeSpacingPx * 3

                val calculatedOffset = -(screenHeight - itemHeight - bottomSafetyMargin).toInt()

                gridState.animateScrollToItem(
                    // +3 because the grid has Spacer, Card (Title), Spacer before the items
                    index = lastRevealedIndex + 3,
                    scrollOffset = calculatedOffset,
                )
            }
        } else if (state.showResultSummary && !wasSummaryShown) {
            gridState.animateScrollToItem(
                index = gridState.layoutInfo.totalItemsCount - 1,
            )
        }
        wasSummaryShown = state.showResultSummary
    }

    LazyVerticalGrid(
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = MaterialTheme.spacing.medium,
            ),
        columns = GridCells.Fixed(count = 3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.large),
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                    text = stringResource(Res.string.overlay_title),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.medium),
            )
        }
        items(
            items = destinations,
            span = {
                GridItemSpan(
                    if (destinations.size > 1) 1 else maxLineSpan,
                )
            },
        ) { destination ->
            DestinationCard(
                destination = destination,
                isRevealed = destination.isRevealed,
                isLarge = destinations.size == 1,
                isNew = destination.isNew,
                mapPointsGained = destination.mapPointsGained,
                onClick = {
                    if (destination.isRevealed) {
                        onAction(HomeAction.ToggleDestinationInfo(destination))

                    } else {
                        onAction(HomeAction.RevealDestination(destination))
                    }
                },
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.medium),
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            AnimatedVisibility(
                visible = showResultSummary,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1F),
                                text = stringResource(Res.string.overlay_destinations_visited),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            Text(
                                text = destinations.size.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1F),
                                text = stringResource(Res.string.overlay_new_destinations),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            Text(
                                text = destinations
                                    .count { it.isNew }
                                    .toString(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1F),
                                text = stringResource(Res.string.overlay_map_points_gained),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                            val density = LocalDensity.current
                            val mapPointsIconSize =
                                with(density) { MaterialTheme.typography.bodyMedium.lineHeight.toDp() }

                            Icon(
                                modifier = Modifier.size(mapPointsIconSize),
                                painter = painterResource(Res.drawable.ic_map_points),
                                contentDescription = stringResource(Res.string.map_points_icon_description),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = formatAmount(
                                    mapPointsGained,
                                    formatMode = FormatMode.Long,
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            PrimaryButton(
                modifier = Modifier
                    .padding(
                        bottom = MaterialTheme.spacing.large,
                        top = MaterialTheme.spacing.medium,
                    )
                    .alpha(0F),
                label = "",
                onClick = { },
            )
        }
    }
}