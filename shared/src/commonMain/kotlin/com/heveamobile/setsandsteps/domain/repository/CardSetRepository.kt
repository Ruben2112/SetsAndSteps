package com.heveamobile.setsandsteps.domain.repository

import com.heveamobile.setsandsteps.domain.model.CardSet
import kotlinx.coroutines.flow.Flow

interface CardSetRepository {
    suspend fun importInitialSetCsvData(
        data: String,
        isActive: Boolean = false,
    )

    suspend fun updateCardSet(cardSet: CardSet)

    suspend fun getCardSetById(id: String): CardSet?
    fun getAllSetProgressFlow(): Flow<List<CardSet>>
}