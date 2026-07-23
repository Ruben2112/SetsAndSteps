package com.heveamobile.setsandsteps.ui.cards

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard

data class CardsState(
    val sets: List<CardSet> = emptyList(),
    val selectedSet: CardSet? = null,
    val cards: List<CollectableCard> = emptyList(),
    val isProgressExpanded: Boolean = false,

    val isLoading: Boolean = false,
)

sealed interface CardsAction {
    data object ToggleCardSetSelector : CardsAction
    data class SelectCardSet(val set: CardSet?) : CardsAction
    data object ToggleProgressDisplay : CardsAction
    data class OpenCardDetails(val cardId: String) : CardsAction
}
