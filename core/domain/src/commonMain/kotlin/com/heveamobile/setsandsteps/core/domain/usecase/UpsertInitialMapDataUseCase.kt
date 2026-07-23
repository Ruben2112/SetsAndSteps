package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.CollectableCardRepository
import com.heveamobile.setsandsteps.core.domain.generated.resources.Res

class UpsertInitialMapDataUseCase(
    val cardSetRepository: CardSetRepository,
    val collectableCardRepository: CollectableCardRepository,
) {
    val currentCardSetVersion = 3
    val initialCardSetId = "4d08314f-2224-4eff-a3bd-8d141d981fad"

    suspend operator fun invoke() {
        val cardSet = cardSetRepository.getCardSetById(id = initialCardSetId)

        val cardSetBytes = Res.readBytes("files/sTheUnitedNations.csv")
        val cardSetData = cardSetBytes.decodeToString()

        val cardBytes = Res.readBytes("files/cTheUnitedNations.csv")
        val cardData = cardBytes.decodeToString()

        cardSet?.let { existingCardSet ->
            if (existingCardSet.version < currentCardSetVersion) {
                val currentCards = collectableCardRepository.getCardsBySetId(initialCardSetId)
                val visitMapping = currentCards.associate {
                    it.id to (it.userData?.findCount
                        ?: 0)
                }

                cardSetRepository.importInitialSetCsvData(
                    data = cardSetData,
                )

                collectableCardRepository.importInitialCardCsvData(data = cardData)

                visitMapping.forEach { (id, visits) ->
                    if (visits > 0) {
                        collectableCardRepository.updateVisitCountForCardById(
                            id = id,
                            visits = visits,
                        )
                    }
                }
            }
        }
            ?: run {
                cardSetRepository.importInitialSetCsvData(
                    data = cardSetData,
                    isActive = true,
                )

                collectableCardRepository.importInitialCardCsvData(data = cardData)
            }
    }
}
