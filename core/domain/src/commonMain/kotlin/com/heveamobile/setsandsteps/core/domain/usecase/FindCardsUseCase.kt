package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.CollectableCardUserData
import com.heveamobile.setsandsteps.core.domain.model.FoundCard
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.CollectableCardRepository

data class FindCardsResult(
    val cards: List<FoundCard> = emptyList(),
    val setPointsGained: Int = 0,
    val levelUpOccurred: Boolean = false,
)

class FindCardsUseCase(
    private val collectableCardRepository: CollectableCardRepository,
    private val cardSetRepository: CardSetRepository,
) {
    suspend operator fun invoke(
        cardSet: CardSet,
        targetRarities: List<Rarity>,
        initialSetPointsDelta: Long = 0,
    ): FindCardsResult {
        val cardSetUserData = cardSet.userData
            ?: return FindCardsResult()
        val cards = cardSet.cards

        var foundCards = emptyList<FoundCard>()
        var levelUpOccurred = false

        val updatedCardUserData = mutableMapOf<String, CollectableCardUserData>()
        fun userDataFor(card: CollectableCard) = updatedCardUserData[card.id]
            ?: card.userData
            ?: CollectableCardUserData()

        run loop@{
            for (targetRarity in targetRarities) {
                val filteredCards = cards.filter { card -> card.rarity == targetRarity }
                if (filteredCards.isEmpty()) continue

                val cardData = filteredCards.random()
                val previousUserData = userDataFor(cardData)
                var foundCard = FoundCard(
                    cardSet = cardSet,
                    card = cardData,
                )

                // Reward set points if the card was already discovered
                if (previousUserData.isDiscovered) {
                    val setPointsGained = when (cardData.rarity) {
                        Rarity.Common -> cardSet.commonValue
                        Rarity.Uncommon -> cardSet.uncommonValue
                        Rarity.Rare -> cardSet.rareValue
                        Rarity.Epic -> cardSet.epicValue
                        Rarity.Legendary -> cardSet.legendaryValue
                    }
                    foundCard = foundCard.copy(
                        setPointsGained = setPointsGained,
                        isNew = false,
                    )
                }

                val newUserData = previousUserData.copy(
                    isDiscovered = true,
                    findCount = previousUserData.findCount + 1,
                )
                updatedCardUserData[cardData.id] = newUserData
                foundCard = foundCard.copy(card = cardData.copy(userData = newUserData))

                // Update Card Set Level when all its cards are discovered
                if (cards.all { userDataFor(it).isDiscovered }) {
                    levelUpOccurred = true

                    // Break the loop to prevent finding more cards
                    return@loop
                }
                foundCards = foundCards + foundCard
            }
        }

        val totalSetPointsGained = foundCards.sumOf { it.setPointsGained }

        if (levelUpOccurred) {
            // Clear discovery flags in DB if level up occurred
            collectableCardRepository.resetDiscovered(cardSetId = cardSet.id)
        } else {
            // Update cards
            collectableCardRepository.upsertCards(foundCards.map { it.card })
        }

        cardSetRepository.updateCardSet(
            cardSet.copy(
                userData = cardSetUserData.copy(
                    currentLevel = if (levelUpOccurred) cardSetUserData.currentLevel + 1 else cardSetUserData.currentLevel,
                    currentSetPoints = if (levelUpOccurred) {
                        0
                    } else {
                        (cardSetUserData.currentSetPoints + initialSetPointsDelta + totalSetPointsGained).coerceAtLeast(0L)
                    },
                ),
            ),
        )

        return FindCardsResult(
            cards = foundCards,
            setPointsGained = totalSetPointsGained,
            levelUpOccurred = levelUpOccurred,
        )
    }
}
