package com.heveamobile.setsandsteps.ui.sets

import com.heveamobile.setsandsteps.domain.model.CardSet

data class SetsState(
    val sets: List<CardSet> = emptyList(),
    val expandedSetId: String? = null,

    val availableSteps: Long = 0L,

    val isLoading: Boolean = false,
)

sealed interface SetsAction {
    data class ViewProgress(val set: CardSet) : SetsAction
    data class ExpandProgress(val set: CardSet) : SetsAction
}