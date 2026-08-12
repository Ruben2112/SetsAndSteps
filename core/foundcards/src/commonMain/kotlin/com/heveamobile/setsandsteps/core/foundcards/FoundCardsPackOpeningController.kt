package com.heveamobile.setsandsteps.core.foundcards

import com.heveamobile.setsandsteps.core.domain.model.FoundCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FoundCardsPackOpeningController(
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<FoundCardsState>,
) {
    private var holdRevealJob: Job? = null

    private fun updateCard(
        setIndex: Int,
        packIndex: Int,
        predicate: (FoundCard) -> Boolean,
        transform: (FoundCard) -> FoundCard,
    ) {
        state.update { state ->
            val packOpeningState = state.packOpeningState
                ?: return@update state
            state.copy(
                packOpeningState = packOpeningState.copy(
                    setPages = packOpeningState.setPages.mapIndexed { si, setPage ->
                        if (si != setIndex) {
                            setPage
                        } else {
                            setPage.copy(
                                packs = setPage.packs.mapIndexed { pi, pack ->
                                    if (pi != packIndex) {
                                        pack
                                    } else {
                                        pack.copy(
                                            cards = pack.cards.map {
                                                if (predicate(it)) transform(it) else it
                                            },
                                        )
                                    }
                                },
                            )
                        }
                    },
                ),
            )
        }
    }

    fun onAction(action: FoundCardsAction.PackOpening) {
        when (action) {
            is FoundCardsAction.PackOpening.RevealPackCard -> {
                updateCard(
                    action.setIndex,
                    action.packIndex,
                    { it.card === action.card },
                ) {
                    it.copy(isRevealed = true)
                }

                val allRevealed = state.value.packOpeningState
                    ?.setPages
                    .orEmpty()
                    .all { it.allRevealed }
                if (allRevealed) {
                    scope.launch {
                        state.update { state ->
                            state.copy(packOpeningState = state.packOpeningState?.copy(showSummaryButton = true))
                        }
                    }
                }
            }

            is FoundCardsAction.PackOpening.StartRevealing -> {
                if (holdRevealJob?.isActive != true) {
                    holdRevealJob = scope.launch {
                        state.update { state ->
                            state.copy(packOpeningState = state.packOpeningState?.copy(isRevealing = true))
                        }

                        val packOpeningState = state.value.packOpeningState
                        val remaining = packOpeningState
                            ?.setPages
                            .orEmpty()
                            .flatMapIndexed { si, setPage ->
                                setPage.packs.flatMapIndexed { pi, pack ->
                                    pack.cards
                                        .filter { !it.isRevealed }
                                        .map {
                                            Triple(
                                                si,
                                                pi,
                                                it,
                                            )
                                        }
                                }
                            }

                        var lastPosition: Pair<Int, Int>? = null
                        remaining.forEachIndexed { index, (si, pi, foundCard) ->
                            val position = si to pi
                            if (position != lastPosition) {
                                // Don't reveal a pack's cards before the pager has actually
                                // scrolled to show it - otherwise reveals race ahead of the
                                // auto-advance delay in PackOpeningPager.
                                state.first { state ->
                                    val packOpeningState = state.packOpeningState
                                    packOpeningState != null &&
                                            packOpeningState.visibleSetIndex == si &&
                                            packOpeningState.visiblePackIndex == pi
                                }
                                lastPosition = position
                            }
                            updateCard(
                                si,
                                pi,
                                { it === foundCard },
                            ) { it.copy(isRevealed = true) }
                            if (index < remaining.size - 1) {
                                delay((remaining[index + 1].third.card.rarity.intValue * 300).toLong())
                            }
                        }

                        state.update { state ->
                            state.copy(packOpeningState = state.packOpeningState?.copy(isRevealing = false))
                        }

                        val allRevealed = state.value.packOpeningState
                            ?.setPages
                            .orEmpty()
                            .all { it.allRevealed }
                        if (allRevealed) {
                            scope.launch {
                                state.update { state ->
                                    state.copy(packOpeningState = state.packOpeningState?.copy(showSummaryButton = true))
                                }
                            }
                        }
                    }
                }
            }

            is FoundCardsAction.PackOpening.UpdateVisiblePack -> {
                state.update { state ->
                    state.copy(
                        packOpeningState = state.packOpeningState?.copy(
                            visibleSetIndex = action.setIndex,
                            visiblePackIndex = action.packIndex,
                        ),
                    )
                }
            }

            is FoundCardsAction.PackOpening.StopRevealing -> {
                holdRevealJob?.cancel()
                holdRevealJob = null

                state.update { state ->
                    state.copy(packOpeningState = state.packOpeningState?.copy(isRevealing = false))
                }
            }

            is FoundCardsAction.PackOpening.ShowPackOpeningSummary -> {
                state.update { state ->
                    state.copy(packOpeningState = state.packOpeningState?.copy(showSummaryScreen = true))
                }
            }
        }
    }
}
