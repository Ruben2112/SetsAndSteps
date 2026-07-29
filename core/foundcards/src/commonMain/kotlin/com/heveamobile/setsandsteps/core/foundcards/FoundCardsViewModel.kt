package com.heveamobile.setsandsteps.core.foundcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoundCardsViewModel(
    private val foundCardsHandler: FoundCardsHandler,
) : ViewModel() {
    private val _state = MutableStateFlow(FoundCardsState())
    val state: StateFlow<FoundCardsState> = _state.asStateFlow()

    private var revealAllJob: Job? = null

    init {
        viewModelScope.launch {
            foundCardsHandler.foundCardsEvents.collectLatest { result ->
                _state.update { state ->
                    state.copy(
                        foundCards = result.cards.sortedBy { it.card.rarity },
                        mapPointsGained = result.setPointsGained,
                    )
                }
            }
        }
    }

    fun onAction(action: FoundCardsAction) {
        when (action) {
            is FoundCardsAction.RevealCard -> {
                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            foundCards = state.foundCards.map { item ->
                                if (item.card === action.card) {
                                    item.copy(isRevealed = true)
                                } else {
                                    item
                                }
                            },
                        )
                    }

                    val allCardsRevealed = _state.value.foundCards.all { it.isRevealed }
                    if (allCardsRevealed) {
                        delay(1000)
                        _state.update { state ->
                            state.copy(showResultSummary = true)
                        }
                    }
                }
            }

            is FoundCardsAction.RevealAllCards -> {
                if (!_state.value.isRevealingAll) {
                    revealAllJob = viewModelScope.launch {
                        _state.update { state ->
                            state.copy(isRevealingAll = true)
                        }

                        // 1. Get the full list of cards to reveal
                        val allCards = _state.value.foundCards

                        // 2. Filter to only those not already revealed
                        val toReveal = allCards.filter { !it.isRevealed }

                        toReveal.forEachIndexed { index, foundCard ->
                            _state.update { state ->
                                state.copy(
                                    foundCards = state.foundCards.map { item ->
                                        if (item === foundCard) {
                                            item.copy(isRevealed = true)
                                        } else {
                                            item
                                        }
                                    },
                                )
                            }

                            // Add a delay to revealing next card.
                            if (index < toReveal.size - 1) {
                                // Logic: Use the rarity of the NEXT card to determine the suspense delay
                                delay((toReveal[index + 1].card.rarity.intValue * 300).toLong())
                            }
                        }

                        _state.update { state ->
                            state.copy(isRevealingAll = false)
                        }
                        delay(1000)
                        _state.update { state ->
                            state.copy(showResultSummary = true)
                        }
                    }
                }
            }

            is FoundCardsAction.CloseFoundCards -> {
                _state.update { FoundCardsState() }
            }

            is FoundCardsAction.ToggleCardInfo -> {
                val cardShown = _state.value.cardShown
                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            cardShown = if (cardShown == null) action.card else null,
                        )
                    }
                }
            }

            is FoundCardsAction.SkipRevealingAllCards -> {
                revealAllJob?.cancel()
                revealAllJob = null

                _state.update { state ->
                    state.copy(
                        foundCards = state.foundCards.map { it.copy(isRevealed = true) },
                        isRevealingAll = false,
                        showResultSummary = true,
                    )
                }
            }
        }
    }
}
