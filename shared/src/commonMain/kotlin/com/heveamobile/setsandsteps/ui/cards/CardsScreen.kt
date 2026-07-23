package com.heveamobile.setsandsteps.ui.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.theme.spacing
import com.heveamobile.setsandsteps.ui.common.Card
import com.heveamobile.setsandsteps.ui.common.CardSetDropDownMenu
import com.heveamobile.setsandsteps.ui.common.CollectableCardLayout
import com.heveamobile.setsandsteps.ui.common.KeyValueRow
import com.heveamobile.setsandsteps.ui.common.SetStatisticsList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.cards_current_level
import setsandsteps.shared.generated.resources.cards_total_visits

@Composable
fun DestinationsScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<CardsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DestinationsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun DestinationsContent(
    modifier: Modifier = Modifier,
    state: CardsState,
    onAction: (CardsAction) -> Unit,
) {
    val selectedSet = state.selectedSet
    val userData = selectedSet?.userData

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        if (state.sets.size > 1) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CardSetDropDownMenu(
                    sets = state.sets,
                    selectedSet = selectedSet
                        ?: state.sets.first(),
                    onItemSelected = { map -> onAction(CardsAction.SelectCardSet(map)) },
                )
            }
        }

        if (selectedSet != null && userData != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onAction(CardsAction.ToggleProgressDisplay)
                        },
                    title = if (state.sets.size > 1) null else selectedSet.name,
                    bottomContent = {
                        KeyValueRow(
                            key = stringResource(Res.string.cards_total_visits),
                            value = selectedSet.cards
                                .sumOf {
                                    it.userData?.findCount
                                        ?: 0
                                }
                                .toString(),
                        )
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    ) {
                        KeyValueRow(
                            modifier = Modifier.padding(end = MaterialTheme.spacing.large),
                            key = stringResource(Res.string.cards_current_level),
                            value = userData.currentLevel.toString(),
                        )
                        SetStatisticsList(
                            set = selectedSet,
                            isExpanded = state.isProgressExpanded,
                        )
                    }
                }
            }

            items(
                selectedSet.cards,
                key = { it.id },
            ) { card ->
                CollectableCardLayout(
                    card = card,
                    isRevealed = card.userData?.isDiscovered
                        ?: false,
                    raritySpoiler = true,
                    onClick = {
                        if (card.userData?.isDiscovered == true) {
                            onAction(CardsAction.OpenCardDetails(card.id))
                        }
                    },
                )
            }
        }
    }
}