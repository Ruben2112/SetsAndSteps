package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first

data class SpendStepsResult(
    val cards: List<FoundCard> = emptyList(),
    val setPointsGained: Int = 0,
    val levelUpOccurred: Boolean = false,
)

data class FoundCard(
    val card: CollectableCard,
    val cardSet: CardSet,
    val isNew: Boolean = true,
    val setPointsGained: Int = 0,
    val isRevealed: Boolean = false,
)

class SpendStepsUseCase(
    private val userRepository: UserRepository,
    private val cardSetRepository: CardSetRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val findCardsUseCase: FindCardsUseCase,
) {
    suspend operator fun invoke(): SpendStepsResult {
        val user = userRepository.getUser()
            ?: return SpendStepsResult()
        val ownedSets = cardSetRepository
            .getAllSetProgressFlow()
            .first()
        val activeCardSet = ownedSets.firstOrNull { it.userData?.isActive == true }
            ?: ownedSets.firstOrNull()
            ?: return SpendStepsResult()
        val activeCardSetUserData = activeCardSet.userData
            ?: return SpendStepsResult()

        // Calculate how many cards we can reward
        val costPerFinding =
            activeCardSetUserData.calculatedDistance * userPreferencesRepository.distanceMultiplier
                .first()
                .toLong()
        val totalPossibleFindings = if (costPerFinding == 0L) 1 else user.availableSteps
            .floorDiv(costPerFinding)
            .toInt()
        if (totalPossibleFindings <= 0) return SpendStepsResult()

        val targetRarities = List(totalPossibleFindings) {
            when ((1..10000).random()) {
                in 1..8109 -> Rarity.Common
                in 8110..9609 -> Rarity.Uncommon
                in 9610..9909 -> Rarity.Rare
                in 9910..9975 -> Rarity.Epic
                else -> Rarity.Legendary
            }
        }

        val result = findCardsUseCase(
            cardSet = activeCardSet,
            targetRarities = targetRarities,
        )

        // Subtract spent steps from user
        userRepository.updateUser(
            user.copy(availableSteps = user.availableSteps - (result.cards.size * costPerFinding)),
        )

        return result
    }
}
