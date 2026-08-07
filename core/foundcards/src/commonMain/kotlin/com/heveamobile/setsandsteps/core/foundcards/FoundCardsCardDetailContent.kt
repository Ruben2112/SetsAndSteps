package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.heveamobile.setsandsteps.core.designsystem.component.CardDetailsCard
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard

@Composable
internal fun CardDetailContent(
    state: FoundCardsState,
    card: CollectableCard,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        CardDetailsCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            contentPadding = PaddingValues(
                top = MaterialTheme.spacing.extraLarge,
                bottom = MaterialTheme.spacing.medium,
            ),
            cardSet = state.findFoundCard(card).cardSet,
            card = card,
        )
    }
}
