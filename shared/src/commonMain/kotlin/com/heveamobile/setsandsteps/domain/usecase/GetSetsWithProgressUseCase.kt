package com.heveamobile.setsandsteps.domain.usecase

import com.heveamobile.setsandsteps.domain.model.CardSet
import com.heveamobile.setsandsteps.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.ui.home.SortingOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSetsWithProgressUseCase(
    private val repository: CardSetRepository,
) {
    operator fun invoke(
        sortOrder: SortingOrder = SortingOrder.Rarity,
        hideUndiscovered: Boolean = false,
    ): Flow<List<CardSet>> {
        return repository
            .getAllSetProgressFlow() // Get all sets
            .map { maps ->
                maps.map { map ->
                    map.copy(
                        // Update the cards to be filtered and sorted accordingly
                        cards = map.cards
                            .filter { if (hideUndiscovered) it.userData?.isDiscovered == true else true }
                            .sortedWith(
                                when (sortOrder) {
                                    SortingOrder.Rarity -> compareBy(
                                        { it.rarity.intValue },
                                        { it.name },
                                    )

                                    SortingOrder.Alphabetical -> compareBy { it.name }
                                    SortingOrder.VisitCount -> compareBy {
                                        it.userData?.findCount
                                            ?: 0
                                    }
                                },
                            ),
                    )
                }
            }
    }
}