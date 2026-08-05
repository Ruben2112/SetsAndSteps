package com.heveamobile.setsandsteps.core.domain.repository

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import kotlinx.coroutines.flow.StateFlow

interface CardSetCatalogRepository {
    val remoteCardSets: StateFlow<List<CardSet>>

    suspend fun refreshCatalog(): Result<Unit>
    suspend fun downloadCardSet(setId: String): Result<Unit>
    suspend fun updateCardSet(setId: String): Result<Unit>
}
