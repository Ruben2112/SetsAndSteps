package com.heveamobile.setsandsteps.feature.sets.presentation

import com.heveamobile.setsandsteps.core.domain.model.CardSet

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
