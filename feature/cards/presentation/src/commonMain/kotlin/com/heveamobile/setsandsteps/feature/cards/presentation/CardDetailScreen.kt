package com.heveamobile.setsandsteps.feature.cards.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.core.designsystem.component.CardDetailsCard
import com.heveamobile.setsandsteps.core.designsystem.component.CardSetDropDownMenu
import com.heveamobile.setsandsteps.core.designsystem.component.DropDownMenu
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.feature.cards.presentation.generated.resources.Res
import com.heveamobile.setsandsteps.feature.cards.presentation.generated.resources.card_details_empty_state
import org.jetbrains.compose.resources.stringResource

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
                    sets = state.sets,
                    selectedSet = state.selectedSet
                        ?: state.sets.first(),
                    onItemSelected = { map -> onAction(CardDetailsAction.SelectSet(map)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (state.cards.isNotEmpty()) {
            item {
                DropDownMenu(
                    items = state.cards,
                    selectedItem = state.selectedCard
                        ?: state.selectedSet?.cards?.first()
                        ?: state.sets.first().cards.first(),
                    onItemSelected = { card ->
                        onAction(CardDetailsAction.SelectCard(card))
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { card ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = card.name,
                        style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                }
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(MaterialTheme.spacing.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.card_details_empty_state),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        textAlign = TextAlign.Center,
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
