package com.heveamobile.setsandsteps.ui.carddetails

import com.heveamobile.setsandsteps.domain.model.CardSet
import com.heveamobile.setsandsteps.domain.model.CollectableCard

data class CardDetailsState(
    val isLoading: Boolean = false,

    val sets: List<CardSet> = emptyList(),
    val selectedSet: CardSet? = null,
    val cards: List<CollectableCard> = emptyList(),
    val selectedCard: CollectableCard? = null,
)

sealed interface CardDetailsAction {
    data class SelectSet(val set: CardSet) : CardDetailsAction
    data class SelectCard(val card: CollectableCard) : CardDetailsAction
}