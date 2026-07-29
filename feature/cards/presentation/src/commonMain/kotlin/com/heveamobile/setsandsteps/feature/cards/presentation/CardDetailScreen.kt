package com.heveamobile.setsandsteps.feature.cards.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.core.designsystem.component.CardDetailsCard
import com.heveamobile.setsandsteps.core.designsystem.component.CardSetDropDownMenu
import com.heveamobile.setsandsteps.core.designsystem.component.DropDownMenu
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing

@Composable
fun CardDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CardDetailsViewModel,
    route: CardDetails,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(route.cardId) {
        viewModel.loadCardDetails(route.cardId)
    }

    CardDetailsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun CardDetailsContent(
    modifier: Modifier = Modifier,
    state: CardDetailsState,
    onAction: (CardDetailsAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        if (state.sets.size > 1) {
            item {
                CardSetDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    sets = state.sets,
                    selectedSet = state.selectedSet
                        ?: state.sets.first(),
                    onItemSelected = { map -> onAction(CardDetailsAction.SelectSet(map)) },
                )
            }
        }

        if (state.cards.isNotEmpty()) {
            item {
                DropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    items = state.cards,
                    selectedItem = state.selectedCard
                        ?: state.selectedSet?.cards?.first()
                        ?: state.sets.first().cards.first(),
                    onItemSelected = { card ->
                        onAction(CardDetailsAction.SelectCard(card))
                    },
                ) { card ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = card.name,
                        style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                }
            }
        }

        if (state.selectedSet != null && state.selectedCard != null) {
            item {
                CardDetailsCard(
                    modifier = Modifier.fillMaxWidth(),
                    cardSet = state.selectedSet,
                    card = state.selectedCard,
                )
            }
        }
    }
}
