package com.heveamobile.setsandsteps.core.foundcards

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.FoundCard

data class FoundCardsState(
    val isLoading: Boolean = false,
    val cardShown: CollectableCard? = null,
    val singlesState: SinglesUiState? = null,
    val packOpeningState: PackOpeningUiState? = null,
) {
    val isPackOpening: Boolean get() = packOpeningState != null

    val isVisible: Boolean
        get() = isLoading || (singlesState?.foundCards?.isNotEmpty() == true) || packOpeningState != null

    fun findFoundCard(card: CollectableCard): FoundCard = if (isPackOpening) {
        packOpeningState!!.setPages
            .flatMap { it.packs.flatMap { pack -> pack.cards } }
            .first { it.card == card }
    } else {
        singlesState!!.foundCards.first { it.card == card }
    }
}

data class SinglesUiState(
    val foundCards: List<FoundCard> = emptyList(),
    val isRevealingAll: Boolean = false,
    val mapPointsGained: Int = 0,
    val showResultSummary: Boolean = false,
)

data class PackOpeningUiState(
    val setPages: List<SetPageUiState>,
    val isRevealing: Boolean = false,
    val showSummaryPage: Boolean = false,
    val showSummaryScreen: Boolean = false,
    val visibleSetIndex: Int = 0,
    val visiblePackIndex: Int = 0,
) {
    /**
     * The (setIndex, packIndex) of the first pack that still has unrevealed cards, in
     * display order, or null if everything is already revealed.
     */
    val firstUnrevealedPosition: Pair<Int, Int>?
        get() {
            setPages.forEachIndexed { setIndex, setPage ->
                val packIndex = setPage.packs.indexOfFirst { !it.allRevealed }
                if (packIndex != -1) return setIndex to packIndex
            }
            return null
        }
}

data class SetPageUiState(
    val cardSet: CardSet,
    val packs: List<PackUiState>,
    val setPointsGained: Int,
) {
    val newCardsCount: Int
        get() = packs.sumOf { pack -> pack.cards.count { it.isRevealed && it.isNew } }

    val pointsRevealedSoFar: Int
        get() = packs.sumOf { pack ->
            pack.cards
                .filter { it.isRevealed }
                .sumOf { it.setPointsGained }
        }

    val allRevealed: Boolean get() = packs.all { it.allRevealed }
}

data class PackUiState(
    val cards: List<FoundCard>,
) {
    val allRevealed: Boolean get() = cards.all { it.isRevealed }
}

sealed interface FoundCardsAction {
    sealed interface Shared : FoundCardsAction {
        data object CloseFoundCards : Shared
        data class ToggleCardInfo(val card: CollectableCard?) : Shared
    }

    sealed interface Singles : FoundCardsAction {
        data class RevealCard(val card: CollectableCard) : Singles
        data object RevealAllCards : Singles
        data object SkipRevealingAllCards : Singles
    }

    sealed interface PackOpening : FoundCardsAction {
        data class RevealPackCard(
            val setIndex: Int,
            val packIndex: Int,
            val card: CollectableCard,
        ) : PackOpening

        data object StartRevealing : PackOpening
        data object StopRevealing : PackOpening
        data object ShowPackOpeningSummary : PackOpening
        data class UpdateVisiblePack(
            val setIndex: Int,
            val packIndex: Int,
        ) : PackOpening
    }
}
