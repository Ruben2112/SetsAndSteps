package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.repository.CardSetCatalogRepository
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCatalogCardSetsUseCase(
    private val cardSetRepository: CardSetRepository,
    private val cardSetCatalogRepository: CardSetCatalogRepository,
) {
    operator fun invoke(): Flow<List<CardSet>> {
        return combine(
            cardSetRepository.getAllSetProgressFlow(),
            cardSetCatalogRepository.getRemoteCardSetsFlow(),
        ) { ownedSets, remoteSets ->
            val ownedIds = ownedSets
                .filter { it.userData?.isOwned == true }
                .map { it.id }
                .toSet()
            remoteSets.filter { it.id !in ownedIds }
        }
    }
}
