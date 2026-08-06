package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSetPacksResult
import com.heveamobile.setsandsteps.core.domain.model.FoundCard
import com.heveamobile.setsandsteps.core.domain.model.ObtainPacksResult
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.core.domain.model.costPerPack
import com.heveamobile.setsandsteps.core.domain.model.packsAvailable
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first

class SpendStepsUseCase(
    private val cardSetRepository: CardSetRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val findCardsUseCase: FindCardsUseCase,
) {
    suspend operator fun invoke(): ObtainPacksResult {
        val activeSets = cardSetRepository.getActiveCardSets()
        val setResults = mutableListOf<CardSetPacksResult>()

        activeSets.forEach { cardSetUserData ->
            val cardSet = cardSetRepository.getCardSetById(cardSetUserData.id)
                ?: return@forEach

            // Calculate how many packs we can reward
            val costPerPack = cardSetUserData.costPerPack(
                userPreferencesRepository.distanceMultiplier.first(),
            )
            var packsToReward = cardSetUserData.packsAvailable(costPerPack)
            if (packsToReward <= 0) return@forEach

            val packs = mutableListOf<List<FoundCard>>()
            var setPointsGained = 0
            var levelUpOccurred = false

            while (packsToReward > 0) {
                val targetRarities = List(5) {
                    when ((1..10000).random()) {
                        in 1..8109 -> Rarity.Common
                        in 8110..9609 -> Rarity.Uncommon
                        in 9610..9909 -> Rarity.Rare
                        in 9910..9975 -> Rarity.Epic
                        else -> Rarity.Legendary
                    }
                }

                val packResult = findCardsUseCase(
                    cardSet = cardSet,
                    targetRarities = targetRarities,
                )

                packs += packResult.cards
                setPointsGained += packResult.setPointsGained
                levelUpOccurred = levelUpOccurred || packResult.levelUpOccurred

                packsToReward--
            }

            cardSetRepository.updateUserData(
                cardSetUserData.copy(
                    currentSetPoints = cardSetUserData.currentSetPoints + setPointsGained,
                    currentSteps = cardSetUserData.currentSteps - (packs.size * costPerPack),
                ),
            )

            setResults += CardSetPacksResult(
                cardSet = cardSet,
                packs = packs,
                setPointsGained = setPointsGained,
                levelUpOccurred = levelUpOccurred,
            )
        }

        return ObtainPacksResult(setResults = setResults)
    }
}
