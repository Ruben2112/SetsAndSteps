package com.heveamobile.setsandsteps.ui.setpointexchange

import com.heveamobile.setsandsteps.domain.model.CardSet
import com.heveamobile.setsandsteps.domain.model.Rarity
import com.heveamobile.setsandsteps.domain.model.User

data class SetPointExchangeState(
    val sets: List<CardSet> = emptyList(),
    val selectedSet: CardSet? = null,
    val user: User? = null,
    val cart: Map<Rarity, Int> = emptyCart,
    val totalCost: Int = 0,
    val amountInStock: Map<Rarity, Int> = emptyMap(),

    val isLoading: Boolean = false,
)

val emptyCart: Map<Rarity, Int>
    get() = mapOf(
        Pair(
            Rarity.Common,
            0,
        ),
        Pair(
            Rarity.Uncommon,
            0,
        ),
        Pair(
            Rarity.Rare,
            0,
        ),
        Pair(
            Rarity.Epic,
            0,
        ),
        Pair(
            Rarity.Legendary,
            0,
        ),
    )

sealed interface SetPointExchangeAction {
    data object ToggleMapSelector : SetPointExchangeAction
    data class SelectSet(val map: CardSet) : SetPointExchangeAction
    data class UpdateCartAmount(
        val rarity: Rarity,
        val amount: Int,
    ) : SetPointExchangeAction

    data object AutofillCart : SetPointExchangeAction
    data object ResetCart : SetPointExchangeAction
    data object Purchase : SetPointExchangeAction
}