package com.heveamobile.setsandsteps.core.foundcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.model.ObtainPacksResult
import com.heveamobile.setsandsteps.core.domain.model.ObtainSinglesResult
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsEvent
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
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

    private val singlesController = FoundCardsSinglesController(
        viewModelScope,
        _state,
    )
    private val packOpeningController = FoundCardsPackOpeningController(
        viewModelScope,
        _state,
    )

    init {
        viewModelScope.launch {
            foundCardsHandler.foundCardsEvents.collectLatest { event ->
                when (event) {
                    FoundCardsEvent.Loading -> _state.update { FoundCardsState(isLoading = true) }

                    FoundCardsEvent.Cleared -> _state.update { FoundCardsState() }

                    is FoundCardsEvent.Result -> _state.update {
                        when (val result = event.result) {
                            is ObtainSinglesResult -> FoundCardsState(
                                singlesState = SinglesUiState(
                                    foundCards = result.allCards.sortedBy { it.card.rarity },
                                    mapPointsGained = result.totalSetPointsGained,
                                ),
                            )

                            is ObtainPacksResult -> FoundCardsState(
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

    fun onAction(action: FoundCardsAction) {
        when (action) {
            is FoundCardsAction.Shared.CloseFoundCards -> {
                _state.update { FoundCardsState() }
            }

            is FoundCardsAction.Shared.ToggleCardInfo -> {
                val cardShown = _state.value.cardShown
                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            cardShown = if (cardShown == null) action.card else null,
                        )
                    }
                }
            }

            is FoundCardsAction.Singles -> singlesController.onAction(action)

            is FoundCardsAction.PackOpening -> packOpeningController.onAction(action)
        }
    }
}
