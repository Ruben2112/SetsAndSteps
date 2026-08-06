package com.heveamobile.setsandsteps.core.domain.model

sealed class ObtainCardsResult {
    abstract val allCards: List<FoundCard>
    abstract val totalSetPointsGained: Int
    abstract val anyLevelUpOccurred: Boolean
}

data class ObtainSinglesResult(
    val cards: List<FoundCard> = emptyList(),
    val setPointsGained: Int = 0,
    val levelUpOccurred: Boolean = false,
) : ObtainCardsResult() {
    override val allCards get() = cards
    override val totalSetPointsGained get() = setPointsGained
    override val anyLevelUpOccurred get() = levelUpOccurred
}

data class CardSetPacksResult(
    val cardSet: CardSet,
    val packs: List<List<FoundCard>> = emptyList(),
    val setPointsGained: Int = 0,
    val levelUpOccurred: Boolean = false,
)

data class ObtainPacksResult(
    val setResults: List<CardSetPacksResult> = emptyList(),
) : ObtainCardsResult() {
    override val allCards get() = setResults.flatMap { it.packs.flatten() }
    override val totalSetPointsGained get() = setResults.sumOf { it.setPointsGained }
    override val anyLevelUpOccurred get() = setResults.any { it.levelUpOccurred }
}
