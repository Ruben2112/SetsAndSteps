package com.heveamobile.setsandsteps.ui.home

import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.SortingOrder
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCard

data class HomeState(
    val isLoadingSteps: Boolean = false,

    val isDrawerOpen: Boolean = false,
    val availableSteps: Long = 0L,
    val requiredSteps: Long = 0L,

    val foundCardsState: FoundCardsState = FoundCardsState(),

    val sharedCardsState: SharedCardsState = SharedCardsState(),
)

data class FoundCardsState(
    val foundCards: List<FoundCard> = emptyList(),
    val newCards: List<CollectableCard> = emptyList(),
    val cardShown: CollectableCard? = null,
    val isRevealingAll: Boolean = false,
    val mapPointsGained: Int = 0,
    val showResultSummary: Boolean = false,
)

data class SharedCardsState(
    val showDropdownMenu: Boolean = false,
    val hideUndiscovered: Boolean = false,
    val sortingOrder: SortingOrder = SortingOrder.Rarity,
)

sealed interface HomeAction {
    data object OpenNavigationDrawer : HomeAction
    data object CloseNavigationDrawer : HomeAction
    data object SyncSteps : HomeAction
    data object SpendSteps : HomeAction

    data class RevealCard(val card: CollectableCard) : HomeAction
    data object RevealAllCards : HomeAction
    data object SkipRevealingAllCards : HomeAction
    data object CloseFoundCards : HomeAction
    data class ToggleCardInfo(val card: CollectableCard?) : HomeAction

    data object ToggleDropdownMenu : HomeAction
    data object ToggleHideUndiscovered : HomeAction
    data class UpdateSortOrder(val sortOrder: SortingOrder) : HomeAction
}
