package com.heveamobile.setsandsteps.feature.setpointexchange.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
import com.heveamobile.setsandsteps.core.domain.usecase.GetCountOfCardsInExchangeStockUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetSetsWithProgressUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetUserUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.PurchaseCardsFromExchangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetPointExchangeViewModel(
    private val getMapsWithProgressUseCase: GetSetsWithProgressUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getCountOfCardsInExchangeStock: GetCountOfCardsInExchangeStockUseCase,
    private val purchaseCardsFromExchangeUseCase: PurchaseCardsFromExchangeUseCase,
    private val foundCardsHandler: FoundCardsHandler,
) : ViewModel() {

    private val _state = MutableStateFlow(SetPointExchangeState())
    val state: StateFlow<SetPointExchangeState> = _state.asStateFlow()

    init {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            getMapsWithProgressUseCase().collect { sets ->
                val selectedSet = sets.first()
                val selectedSetUserData = selectedSet.userData ?: return@collect

                _state.update { state ->
                    state.copy(
                        sets = sets,
                        selectedSet = selectedSet,
                        amountInStock = Rarity.entries.associateWith {
                            getCountOfCardsInExchangeStock(
                                selectedSetUserData,
                                it,
                            )
                        },
                    )
                }
            }

            viewModelScope.launch(Dispatchers.IO) {
                getUserUseCase().first()
                getMapsWithProgressUseCase().first()

                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: SetPointExchangeAction) {
        when (action) {
            is SetPointExchangeAction.AutofillCart -> {
                _state.update { state ->
                    val set = state.selectedSet?.userData
                        ?: return@update state

                    val newCart = Rarity.entries.associateWith { rarity ->
                        getCountOfCardsInExchangeStock(
                            cardSet = set,
                            rarity = rarity,
                        )
                    }

                    state.copy(
                        cart = newCart,
                        totalCost = calculateTotalCost(
                            selectedSet = state.selectedSet,
                            cart = newCart,
                        ),
                    )
                }
            }

            is SetPointExchangeAction.Purchase -> {
                val currentState = _state.value
                val set = currentState.selectedSet
                val setUserData = set?.userData ?: return

                if (currentState.totalCost == 0 || setUserData.currentSetPoints < currentState.totalCost) return

                viewModelScope.launch(Dispatchers.IO) {
                    _state.value.selectedSet?.let { set ->
                        val result = purchaseCardsFromExchangeUseCase(
                            cardSet = set,
                            cart = currentState.cart,
                            cost = currentState.totalCost,
                        )
                        if (result.cards.isNotEmpty()) {
                            foundCardsHandler.announceFoundCards(result)
                        }
                    }
                }

                _state.update { state ->
                    state.copy(
                        cart = emptyCart,
                        totalCost = 0,
                    )
                }
            }

            is SetPointExchangeAction.ResetCart -> {
                _state.update { state ->
                    state.copy(
                        cart = emptyCart,
                        totalCost = 0,
                    )
                }
            }

            is SetPointExchangeAction.SelectSet -> TODO()
            is SetPointExchangeAction.ToggleMapSelector -> TODO()
            is SetPointExchangeAction.UpdateCartAmount -> {
                _state.update { state ->
                    val correctedAmount = coerceAmount(
                        action.amount,
                        action.rarity,
                    )
                    val newCart = state.cart
                        .toMutableMap()
                        .apply {
                            this[action.rarity] = correctedAmount
                        }

                    state.copy(
                        cart = newCart,
                        totalCost = calculateTotalCost(
                            state.selectedSet,
                            newCart,
                        ),
                    )
                }
            }
        }
    }

    private fun calculateTotalCost(
        selectedSet: CardSet?,
        cart: Map<Rarity, Int>,
    ): Int {
        if (selectedSet == null) return 0
        return cart.entries.sumOf { (rarity, amount) ->
            amount * selectedSet.storePrice(rarity)
        }
    }

    private fun coerceAmount(
        amount: Int,
        rarity: Rarity,
    ): Int {
        val set = _state.value.selectedSet?.userData
            ?: return 0
        val maxAllowed = getCountOfCardsInExchangeStock(
            set,
            rarity,
        )
        return amount.coerceIn(
            0,
            maxAllowed,
        )
    }
}
