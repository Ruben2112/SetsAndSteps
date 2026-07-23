package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.Rarity

class PurchaseCardsFromExchangeUseCase(
    private val findCardsUseCase: FindCardsUseCase,
) {
    suspend operator fun invoke(
        cardSet: CardSet,
        cart: Map<Rarity, Int>,
        cost: Int,
    ): SpendStepsResult {
        val userData = cardSet.userData
            ?: return SpendStepsResult()
        if (cost <= 0 || userData.currentSetPoints < cost) return SpendStepsResult()

        val targetRarities = cart.entries.flatMap { (rarity, count) -> List(count) { rarity } }
        if (targetRarities.isEmpty()) return SpendStepsResult()

        return findCardsUseCase(
            cardSet = cardSet,
            targetRarities = targetRarities,
            initialSetPointsDelta = -cost.toLong(),
        )
    }
}
