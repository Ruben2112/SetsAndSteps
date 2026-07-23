package com.heveamobile.setsandsteps.feature.cards.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.usecase.GetSetsWithProgressUseCase
import com.heveamobile.setsandsteps.core.navigation.NavigationHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CardsViewModel(
    private val navigationHandler: NavigationHandler,
    private val getSetsWithProgressUseCase: GetSetsWithProgressUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CardsState())
    val state: StateFlow<CardsState> = _state.asStateFlow()

    init {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            combine(
                userPreferencesRepository.gridSortingOrder,
                userPreferencesRepository.hideUndiscovered,
            ) { order, hide -> order to hide }
                .flatMapLatest { (order, hide) ->
                    getSetsWithProgressUseCase(
                        order,
                        hide,
                    )
                }
                .collectLatest { sets ->
                    _state.update { state ->
                        val currentSelectedId = state.selectedSet?.id
                        val selectedMap = sets.find { it.id == currentSelectedId }
                            ?: sets.firstOrNull()

                        state.copy(
                            selectedSet = selectedMap,
                            sets = sets,
                            cards = selectedMap?.cards
                                ?: emptyList(),
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun onAction(action: CardsAction) {
        when (action) {
            is CardsAction.ToggleCardSetSelector -> TODO()
            is CardsAction.SelectCardSet -> {
                _state.update { it.copy(selectedSet = action.set) }
            }

            is CardsAction.ToggleProgressDisplay -> {
                _state.update { it.copy(isProgressExpanded = !it.isProgressExpanded) }
            }

            is CardsAction.OpenCardDetails -> {
                navigationHandler.navigateTo(DestinationInfo(action.cardId))
            }
        }
    }
}
