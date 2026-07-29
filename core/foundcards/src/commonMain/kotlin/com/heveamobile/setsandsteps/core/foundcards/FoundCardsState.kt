package com.heveamobile.setsandsteps.core.foundcards

import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCard

data class FoundCardsState(
    val foundCards: List<FoundCard> = emptyList(),
    val cardShown: CollectableCard? = null,
    val isRevealingAll: Boolean = false,
    val mapPointsGained: Int = 0,
    val showResultSummary: Boolean = false,
)

sealed interface FoundCardsAction {
    data class RevealCard(val card: CollectableCard) : FoundCardsAction
    data object RevealAllCards : FoundCardsAction
    data object SkipRevealingAllCards : FoundCardsAction
    data object CloseFoundCards : FoundCardsAction
    data class ToggleCardInfo(val card: CollectableCard?) : FoundCardsAction
}
