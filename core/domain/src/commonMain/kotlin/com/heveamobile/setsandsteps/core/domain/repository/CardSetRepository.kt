package com.heveamobile.setsandsteps.core.domain.repository

import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CardSetUserData
import kotlinx.coroutines.flow.Flow

interface CardSetRepository {
    suspend fun importInitialSetCsvData(
        data: String,
        isActive: Boolean = false,
    )

    suspend fun updateCardSet(cardSet: CardSet)

    suspend fun toggleActiveState(setId: String)

    suspend fun getCardSetById(id: String): CardSet?

    suspend fun getActiveCardSets(): List<CardSetUserData>
    fun getAllSetProgressFlow(): Flow<List<CardSet>>
    suspend fun updateUserData(userData: CardSetUserData)
}