package com.heveamobile.setsandsteps.core.foundcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.model.FoundCard
import com.heveamobile.setsandsteps.core.domain.model.ObtainPacksResult
import com.heveamobile.setsandsteps.core.domain.model.ObtainSinglesResult
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsEvent
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoundCardsViewModel(
    private val foundCardsHandler: FoundCardsHandler,
) : ViewModel() {
    private val _state = MutableStateFlow(FoundCardsState())
    val state: StateFlow<FoundCardsState> = _state.asStateFlow()

    private var revealAllJob: Job? = null
    private var holdRevealJob: Job? = null

    init {
        viewModelScope.launch {
            foundCardsHandler.foundCardsEvents.collectLatest { event ->
                when (event) {
                    FoundCardsEvent.Loading -> _state.update { FoundCardsState(isLoading = true) }

                    FoundCardsEvent.Cleared -> _state.update { FoundCardsState() }

                    is FoundCardsEvent.Result -> _state.update {
                        when (val result = event.result) {
                            is ObtainSinglesResult -> FoundCardsState(
                                foundCards = result.allCards.sortedBy { it.card.rarity },
                                mapPointsGained = result.totalSetPointsGained,
                                isPackOpening = false,
                            )

                            is ObtainPacksResult -> FoundCardsState(
                                isPackOpening = true,
                                mapPointsGained = result.totalSetPointsGained,
                                packOpeningState = PackOpeningUiState(
                                    setPages = result.setResults.map { setResult ->
                                        SetPageUiState(
                                            cardSet = setResult.cardSet,
                                            packs = setResult.packs.map { pack -> PackUiState(cards = pack) },
                                            setPointsGained = setResult.setPointsGained,
                                        )
                                    },
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateCard(
        setIndex: Int,
        packIndex: Int,
        predicate: (FoundCard) -> Boolean,
        transform: (FoundCard) -> FoundCard,
    ) {
        _state.update { state ->
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

            is FoundCardsAction.RevealPackCard -> {
                updateCard(
                    action.setIndex,
                    action.packIndex,
                    { it.card === action.card },
                ) {
                    it.copy(isRevealed = true)
                }
            }

            is FoundCardsAction.StartRevealing -> {
                if (holdRevealJob?.isActive != true) {
                    holdRevealJob = viewModelScope.launch {
                        _state.update { state ->
                            state.copy(packOpeningState = state.packOpeningState?.copy(isRevealing = true))
                        }

                        val packOpeningState = _state.value.packOpeningState
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
                                _state.first { state ->
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

                        _state.update { state ->
                            state.copy(packOpeningState = state.packOpeningState?.copy(isRevealing = false))
                        }

                        val allRevealed = _state.value.packOpeningState
                            ?.setPages
                            .orEmpty()
                            .all { it.allRevealed }
                        if (allRevealed) {
                            viewModelScope.launch {
                                delay(1000)
                                _state.update { state ->
                                    state.copy(packOpeningState = state.packOpeningState?.copy(showSummaryPage = true))
                                }
                            }
                        }
                    }
                }
            }

            is FoundCardsAction.UpdateVisiblePack -> {
                _state.update { state ->
                    state.copy(
                        packOpeningState = state.packOpeningState?.copy(
                            visibleSetIndex = action.setIndex,
                            visiblePackIndex = action.packIndex,
                        ),
                    )
                }
            }

            is FoundCardsAction.StopRevealing -> {
                holdRevealJob?.cancel()
                holdRevealJob = null

                _state.update { state ->
                    state.copy(packOpeningState = state.packOpeningState?.copy(isRevealing = false))
                }
            }

            is FoundCardsAction.ShowPackOpeningSummary -> {
                _state.update { state ->
                    state.copy(packOpeningState = state.packOpeningState?.copy(showSummaryScreen = true))
                }
            }
        }
    }
}
