package com.heveamobile.setsandsteps.core.domain.repository

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import kotlinx.coroutines.flow.Flow

interface CardSetRepository {
    suspend fun importInitialSetCsvData(
        data: String,
        isActive: Boolean = false,
    )

    suspend fun updateCardSet(cardSet: CardSet)

    suspend fun toggleActiveState(setId: String)

    suspend fun getCardSetById(id: String): CardSet?
    fun getAllSetProgressFlow(): Flow<List<CardSet>>
}