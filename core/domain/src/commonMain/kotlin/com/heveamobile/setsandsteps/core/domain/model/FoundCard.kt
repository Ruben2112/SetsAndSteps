package com.heveamobile.setsandsteps.core.domain.model

data class FoundCard(
    val card: CollectableCard,
    val cardSet: CardSet,
    val isNew: Boolean = true,
    val setPointsGained: Int = 0,
    val isRevealed: Boolean = false,
)
