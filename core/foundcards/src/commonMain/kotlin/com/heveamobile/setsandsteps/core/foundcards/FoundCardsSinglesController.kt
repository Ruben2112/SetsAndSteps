package com.heveamobile.setsandsteps.core.foundcards

import com.heveamobile.setsandsteps.core.designsystem.component.animationTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FoundCardsSinglesController(
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<FoundCardsState>,
) {
    private var revealAllJob: Job? = null

    fun onAction(action: FoundCardsAction.Singles) {
        when (action) {
            is FoundCardsAction.Singles.RevealCard -> {
                scope.launch {
                    state.update { state ->
                        state.copy(
                            singlesState = state.singlesState?.copy(
                                foundCards = state.singlesState.foundCards.map { item ->
                                    if (item.card === action.card) {
                                        item.copy(isRevealed = true)
                                    } else {
                                        item
                                    }
                                },
                            ),
                        )
                    }

                    val allCardsRevealed = state.value.singlesState?.foundCards
                        .orEmpty()
                        .all { it.isRevealed }
                    if (allCardsRevealed) {
                        delay(action.card.animationTime.toLong())
                        state.update { state ->
                            state.copy(singlesState = state.singlesState?.copy(showResultSummary = true))
                        }
                    }
                }
            }

            is FoundCardsAction.Singles.RevealAllCards -> {
                if (state.value.singlesState?.isRevealingAll != true) {
                    revealAllJob = scope.launch {
                        state.update { state ->
                            state.copy(singlesState = state.singlesState?.copy(isRevealingAll = true))
                        }

                        // 1. Get the full list of cards to reveal
                        val allCards = state.value.singlesState?.foundCards.orEmpty()

                        // 2. Filter to only those not already revealed
                        val toReveal = allCards.filter { !it.isRevealed }

                        toReveal.forEachIndexed { index, foundCard ->
                            state.update { state ->
                                state.copy(
                                    singlesState = state.singlesState?.copy(
                                        foundCards = state.singlesState.foundCards.map { item ->
                                            if (item === foundCard) {
                                                item.copy(isRevealed = true)
                                            } else {
                                                item
                                            }
                                        },
                                    ),
                                )
                            }

                            // Add a delay to revealing next card.
                            if (index < toReveal.size - 1) {
                                // Logic: Use the rarity of the current card to determine the suspense delay
                                delay(toReveal[index].card.animationTime.toLong())
                            }
                        }

                        state.update { state ->
                            state.copy(singlesState = state.singlesState?.copy(isRevealingAll = false))
                        }
                        delay(toReveal.last().card.animationTime.toLong())
                        state.update { state ->
                            state.copy(singlesState = state.singlesState?.copy(showResultSummary = true))
                        }
                    }
                }
            }

            is FoundCardsAction.Singles.SkipRevealingAllCards -> {
                revealAllJob?.cancel()
                revealAllJob = null

                state.update { state ->
                    state.copy(
                        singlesState = state.singlesState?.copy(
                            foundCards = state.singlesState.foundCards.map { it.copy(isRevealed = true) },
                            isRevealingAll = false,
                            showResultSummary = true,
                        ),
                    )
                }
            }
        }
    }
}
