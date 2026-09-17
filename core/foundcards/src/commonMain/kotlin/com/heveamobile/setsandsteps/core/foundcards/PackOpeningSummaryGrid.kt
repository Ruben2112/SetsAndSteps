package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.heveamobile.setsandsteps.core.designsystem.component.CollectableCardLayout
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing

@Composable
fun PackOpeningSummaryGrid(
    modifier: Modifier = Modifier,
    packOpeningState: PackOpeningUiState,
    onAction: (FoundCardsAction) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium),
        columns = GridCells.Fixed(count = 3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        packOpeningState.setPages.forEach { setPageUiState ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                SummarySetHeader(
                    cardSet = setPageUiState.cardSet,
                    packsOpened = setPageUiState.packs.size,
                    pointsGained = setPageUiState.pointsRevealedSoFar,
                    newCards = setPageUiState.newCardsCount,
                )
            }

            val allCards = setPageUiState.packs.flatMap { it.cards }
            val (newCards, oldCards) = allCards.partition { it.isNew }

            items(
                items = newCards.sortedBy { it.card.rarity },
            ) { foundCard ->
                CollectableCardLayout(
                    backsideImageUrl = foundCard.cardSet.backsideImageUrl,
                    card = foundCard.card,
                    isRevealed = true,
                    isNew = foundCard.isNew,
                    mapPointsGained = foundCard.setPointsGained,
                    onClick = { onAction(FoundCardsAction.Shared.ToggleCardInfo(foundCard.card)) },
                )
            }

            if (newCards.isNotEmpty() && oldCards.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }

            items(
                items = oldCards.sortedBy { it.card.rarity },
            ) { foundCard ->
                CollectableCardLayout(
                    backsideImageUrl = foundCard.cardSet.backsideImageUrl,
                    card = foundCard.card,
                    isRevealed = true,
                    isNew = foundCard.isNew,
                    mapPointsGained = foundCard.setPointsGained,
                    onClick = { onAction(FoundCardsAction.Shared.ToggleCardInfo(foundCard.card)) },
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                // Reserves space at the end of the scrollable content so the close button
                // doesn't cover the last row of cards.
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.spacing.extraLarge),
                )
            }
        }
    }
}
