package com.heveamobile.setsandsteps.shell

import com.heveamobile.setsandsteps.core.domain.model.SortingOrder

data class HomeState(
    val isLoadingSteps: Boolean = false,

    val isDrawerOpen: Boolean = false,
    val availableSteps: Long = 0L,
    val requiredSteps: Long = 0L,

    val sharedCardsState: SharedCardsState = SharedCardsState(),
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

    data object ToggleDropdownMenu : HomeAction
    data object ToggleHideUndiscovered : HomeAction
    data class UpdateSortOrder(val sortOrder: SortingOrder) : HomeAction
}
