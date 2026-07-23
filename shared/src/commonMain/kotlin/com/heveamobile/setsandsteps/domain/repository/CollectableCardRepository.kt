package com.heveamobile.setsandsteps.domain.repository

import com.heveamobile.setsandsteps.domain.model.CollectableCard

interface CollectableCardRepository {
    fun getCardsBySetId(cardSetId: String): List<CollectableCard>
    fun getCardById(id: String): CollectableCard?
    suspend fun importInitialCardCsvData(data: String)
    suspend fun resetDiscovered(cardSetId: String)
    suspend fun upsertCards(cards: List<CollectableCard>)
    suspend fun updateVisitCountForCardById(
        id: String,
        visits: Int,
    )
}