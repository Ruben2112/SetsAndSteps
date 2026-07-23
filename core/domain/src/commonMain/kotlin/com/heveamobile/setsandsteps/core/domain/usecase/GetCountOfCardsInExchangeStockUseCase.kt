package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSetUserData
import com.heveamobile.setsandsteps.core.domain.model.Rarity

class GetCountOfCardsInExchangeStockUseCase {
    operator fun invoke(
        cardSet: CardSetUserData,
        rarity: Rarity,
    ): Int {
        val total = when (rarity) {
            Rarity.Common -> cardSet.commonCardCount
            Rarity.Uncommon -> cardSet.uncommonCardCount
            Rarity.Rare -> cardSet.rareCardCount
            Rarity.Epic -> cardSet.epicCardCount
            Rarity.Legendary -> cardSet.legendaryCardCount
        }
        val found = when (rarity) {
            Rarity.Common -> cardSet.commonCardsFound
            Rarity.Uncommon -> cardSet.uncommonCardsFound
            Rarity.Rare -> cardSet.rareCardsFound
            Rarity.Epic -> cardSet.epicCardsFound
            Rarity.Legendary -> cardSet.legendaryCardsFound
        }

        // total = amount of destinations in map
        // visited = amount of destinations that were already visited
        return (total - found).coerceAtLeast(0)
    }
}