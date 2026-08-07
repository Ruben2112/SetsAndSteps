package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.CollectableCardLayout
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.formatAmount
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.Res
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_cards_found
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_new_cards
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_set_points_gained
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.overlay_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SinglesGrid(
    singlesState: SinglesUiState,
    onAction: (FoundCardsAction) -> Unit,
    screenHeight: Int,
    screenWidth: Int,
) {
    val foundCards = singlesState.foundCards
    val showResultSummary = singlesState.showResultSummary
    val mapPointsGained = singlesState.mapPointsGained

    val gridState = rememberLazyGridState()

    val density = LocalDensity.current
    val smallSpacingPx = with(density) { MaterialTheme.spacing.small.toPx() }
    val mediumSpacingPx = with(density) { MaterialTheme.spacing.medium.toPx() }
    val largeSpacingPx = with(density) { MaterialTheme.spacing.large.toPx() }

    // Auto scroll logic for revealing all cards
    var wasSummaryShown by remember { mutableStateOf(singlesState.showResultSummary) }
    LaunchedEffect(
        foundCards.filter { it.isRevealed },
        singlesState.isRevealingAll,
        singlesState.showResultSummary,
    ) {
        if (singlesState.isRevealingAll) {
            val lastRevealedIndex = foundCards.indexOfLast { it.isRevealed }

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
//             +3 because the grid has Spacer, Card (Title), Spacer before the items
                    index = lastRevealedIndex + 3,
                    scrollOffset = calculatedOffset,
                )
            }
        } else if (singlesState.showResultSummary && !wasSummaryShown) {
            gridState.animateScrollToItem(
                index = gridState.layoutInfo.totalItemsCount - 1,
            )
        }
        wasSummaryShown = singlesState.showResultSummary
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
            items = foundCards,
            span = {
                GridItemSpan(
                    if (foundCards.size > 1) 1 else maxLineSpan,
                )
            },
        ) { card ->
            CollectableCardLayout(
                card = card.card,
                isRevealed = card.isRevealed,
                isLarge = foundCards.size == 1,
                isNew = card.isNew,
                mapPointsGained = card.setPointsGained,
                onClick = {
                    if (card.isRevealed) {
                        onAction(FoundCardsAction.Shared.ToggleCardInfo(card.card))

                    } else {
                        onAction(FoundCardsAction.Singles.RevealCard(card.card))
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
                                text = stringResource(Res.string.overlay_cards_found),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            Text(
                                text = foundCards.size.toString(),
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
                                text = stringResource(Res.string.overlay_new_cards),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            Text(
                                text = foundCards
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
                                text = stringResource(Res.string.overlay_set_points_gained),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
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
            // Reserves the same height as the floating reveal/skip button so the grid
            // stays vertically balanced instead of being covered by it.
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonDefaults.MinHeight)
                    .padding(
                        bottom = MaterialTheme.spacing.large,
                        top = MaterialTheme.spacing.medium,
                    ),
            )
        }
    }
}
