package com.heveamobile.setsandsteps.feature.sets.presentation

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CardSetDownloadState

enum class SetsTab {
    MySets,
    Catalog,
}

data class SetsState(
    val selectedTab: SetsTab = SetsTab.MySets,
    val sets: List<CardSet> = emptyList(),
    val catalogSets: List<CardSet> = emptyList(),
    val updateAvailableSetIds: Set<String> = emptySet(),
    val downloadStates: Map<String, CardSetDownloadState> = emptyMap(),
    val expandedSetId: String? = null,

    val availableSteps: Long = 0L,

    val isLoading: Boolean = false,
)

sealed interface SetsAction {
    data class ViewProgress(val set: CardSet) : SetsAction
    data class ExpandProgress(val set: CardSet) : SetsAction
    data class SelectTab(val tab: SetsTab) : SetsAction
    data class DownloadSet(val setId: String) : SetsAction
    data class UpdateSet(val setId: String) : SetsAction
    data class ToggleActiveState(val set: CardSet) : SetsAction
}

sealed interface SetsEvent {
    data class PurchaseSucceeded(val setName: String) : SetsEvent
    data class DownloadFailed(val setName: String) : SetsEvent
    data class UpdateFailed(val setName: String) : SetsEvent
}
