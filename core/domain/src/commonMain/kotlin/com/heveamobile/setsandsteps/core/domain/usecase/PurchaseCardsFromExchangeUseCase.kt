package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.ObtainSinglesResult
import com.heveamobile.setsandsteps.core.domain.model.Rarity

class PurchaseCardsFromExchangeUseCase(
    private val findCardsUseCase: FindCardsUseCase,
) {
    suspend operator fun invoke(
        cardSet: CardSet,
        cart: Map<Rarity, Int>,
        cost: Int,
    ): ObtainSinglesResult {
        val userData = cardSet.userData
            ?: return ObtainSinglesResult()
        if (cost <= 0 || userData.currentSetPoints < cost) return ObtainSinglesResult()

        val targetRarities = cart.entries.flatMap { (rarity, count) -> List(count) { rarity } }
        if (targetRarities.isEmpty()) return ObtainSinglesResult()

        val result = findCardsUseCase(
            cardSet = cardSet,
            targetRarities = targetRarities,
            initialSetPointsDelta = -cost.toLong(),
        )

        return ObtainSinglesResult(
            cards = result.cards,
            setPointsGained = result.setPointsGained,
            levelUpOccurred = result.levelUpOccurred,
        )
    }
}
