package com.heveamobile.setsandsteps.core.data.repository

import com.heveamobile.setsandsteps.core.database.dao.CollectableCardDao
import com.heveamobile.setsandsteps.core.database.dao.CollectableCardUserDataDao
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardUserDataEntity
import com.heveamobile.setsandsteps.core.data.mapper.toDomain
import com.heveamobile.setsandsteps.core.data.mapper.toEntity
import com.heveamobile.setsandsteps.core.data.mapper.toUserDataEntity
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.repository.CollectableCardRepository

class CollectableCardRepositoryImpl(
    private val collectableCardDao: CollectableCardDao,
    private val collectableCardUserDataDao: CollectableCardUserDataDao,
) : CollectableCardRepository {
    override fun getCardsBySetId(cardSetId: String): List<CollectableCard> {
        return collectableCardDao
            .getCardsByCardSetId(cardSetId)
            .map { it.toDomain() }
    }

    override fun getCardById(id: String): CollectableCard? {
        return collectableCardDao
            .getCardById(id = id)
            ?.toDomain()
    }

    override suspend fun importInitialCardCsvData(data: String) {
        val lines = data.lines()
        val cards = lines
            .drop(1)
            .mapNotNull { line ->
                if (line.isBlank()) return@mapNotNull null
                val columns = line.split(";")
                CollectableCardEntity(
                    id = columns[0],
                    cardSetId = columns[1],
                    name = columns[2],
                    rarity = columns[3].toInt(),
                    imageUrl = columns[4],
                    bbox = columns[5],
                    propertyValue1 = columns.getOrNull(6),
                    propertyValue2 = columns.getOrNull(7),
                    propertyValue3 = columns.getOrNull(8),
                    propertyValue4 = columns.getOrNull(9),
                    propertyValue5 = columns.getOrNull(10),
                    propertyValue6 = columns.getOrNull(11),
                    propertyValue7 = columns.getOrNull(12),
                    propertyValue8 = columns.getOrNull(13),
                    propertyValue9 = columns.getOrNull(14),
                    propertyValue10 = columns.getOrNull(15),
                )
            }

        collectableCardDao.upsertCards(cards)
        cards.forEach { card ->
            collectableCardUserDataDao.insertIfAbsent(CollectableCardUserDataEntity(id = card.id))
        }
    }

    override suspend fun resetDiscovered(cardSetId: String) {
        collectableCardUserDataDao.resetDiscovered(cardSetId = cardSetId)
    }

    override suspend fun upsertCards(cards: List<CollectableCard>) {
        collectableCardDao.upsertCards(cards.map { it.toEntity() })
        cards.forEach { card ->
            collectableCardUserDataDao.upsert(card.toUserDataEntity())
        }
    }

    override suspend fun updateVisitCountForCardById(
        id: String,
        visits: Int,
    ) {
        collectableCardUserDataDao.updateFindCount(
            id = id,
            count = visits,
        )
    }
}