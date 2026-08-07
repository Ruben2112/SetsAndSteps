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
                SetHeader(
                    setName = setPageUiState.cardSet.name,
                    packProgressValue = setPageUiState.packs.size.toString(),
                    newCardsCount = setPageUiState.newCardsCount,
                    pointsGained = setPageUiState.pointsRevealedSoFar,
                )
            }

            items(
                items = setPageUiState.packs
                    .flatMap { it.cards }
                    .sortedBy { it.card.rarity },
            ) { foundCard ->
                CollectableCardLayout(
                    card = foundCard.card,
                    isRevealed = true,
                    isNew = foundCard.isNew,
                    mapPointsGained = foundCard.setPointsGained,
                    onClick = { onAction(FoundCardsAction.ToggleCardInfo(foundCard.card)) },
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
