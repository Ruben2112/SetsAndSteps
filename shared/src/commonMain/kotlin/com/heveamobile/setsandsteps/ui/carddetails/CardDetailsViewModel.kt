package com.heveamobile.setsandsteps.ui.carddetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.domain.model.CardSet
import com.heveamobile.setsandsteps.domain.model.CollectableCard
import com.heveamobile.setsandsteps.domain.repository.CollectableCardRepository
import com.heveamobile.setsandsteps.domain.usecase.GetSetsWithProgressUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardDetailsViewModel(
    private val collectableCardRepository: CollectableCardRepository,
    private val getSetsWithProgressUseCase: GetSetsWithProgressUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CardDetailsState())
    val state: StateFlow<CardDetailsState> = _state.asStateFlow()

    fun loadCardDetails(cardId: String?) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            getSetsWithProgressUseCase().collectLatest { sets ->
                if (sets.isEmpty()) {
                    _state.update { it.copy(isLoading = false) }
                    return@collectLatest
                }

                var selectedCard: CollectableCard? =
                    if (cardId != null) collectableCardRepository.getCardById(cardId) else _state.value.selectedCard

                val selectedSet: CardSet? = if (selectedCard == null) {
                    sets.firstOrNull()
                } else {
                    sets.firstOrNull { it.id == selectedCard.cardSetId }
                }

                if (selectedSet == null) return@collectLatest

                if (selectedCard == null) {
                    selectedCard = selectedSet.cards
                        .filter { it.userData?.isDiscovered == true }
                        .minByOrNull { it.name }
                }

                _state.update { state ->
                    state.copy(
                        sets = sets,
                        selectedSet = selectedSet,
                        cards = selectedSet.cards
                            .filter { it.userData?.isDiscovered == true }
                            .sortedBy { it.name },
                        selectedCard = selectedCard,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onAction(action: CardDetailsAction) {
        when (action) {
            is CardDetailsAction.SelectCard -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { state ->
                    state.copy(
                        selectedCard = action.card,
                    )
                }
            }

            is CardDetailsAction.SelectSet -> viewModelScope.launch(Dispatchers.IO) {
                val set = action.set
                val card = set.cards
                    .filter { it.userData?.isDiscovered == true }
                    .minByOrNull { it.name }

                _state.update { state ->
                    state.copy(
                        selectedSet = set,
                        cards = set.cards
                            .filter { it.userData?.isDiscovered == true }
                            .sortedBy { it.name },
                        selectedCard = card,
                    )
                }
            }
        }
    }
}