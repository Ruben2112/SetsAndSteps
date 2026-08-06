package com.heveamobile.setsandsteps.core.data.repository

import com.heveamobile.setsandsteps.core.data.mapper.toDomain
import com.heveamobile.setsandsteps.core.data.mapper.toEntity
import com.heveamobile.setsandsteps.core.data.mapper.toUserData
import com.heveamobile.setsandsteps.core.data.mapper.toUserDataEntity
import com.heveamobile.setsandsteps.core.database.dao.CardSetDao
import com.heveamobile.setsandsteps.core.database.dao.CardSetUserDataDao
import com.heveamobile.setsandsteps.core.database.dao.CollectableCardDao
import com.heveamobile.setsandsteps.core.database.entity.CardSetUserDataEntity
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CardSetUserData
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class CardSetRepositoryImpl(
    private val cardSetDao: CardSetDao,
    private val cardSetUserDataDao: CardSetUserDataDao,
    private val collectableCardDao: CollectableCardDao,
) : CardSetRepository {
    override suspend fun importInitialSetCsvData(
        data: String,
        isActive: Boolean,
    ) {
        val lines = data.lines()
        val cardSet = lines
            .drop(1)
            .mapNotNull { line ->
                if (line.isBlank()) return@mapNotNull null
                val columns = line.split(";")
                CardSet(
                    id = columns[0],
                    name = columns[1],
                    version = columns[2].toInt(),
                    baseDistance = columns[3].toLong(),
                    commonValue = columns[4].toInt(),
                    uncommonValue = columns[5].toInt(),
                    rareValue = columns[6].toInt(),
                    epicValue = columns[7].toInt(),
                    legendaryValue = columns[8].toInt(),
                    backsideImageUrl = columns[9],
                    propertyName1 = columns.getOrNull(10),
                    propertyName2 = columns.getOrNull(11),
                    propertyName3 = columns.getOrNull(12),
                    propertyName4 = columns.getOrNull(13),
                    propertyName5 = columns.getOrNull(14),
                    propertyName6 = columns.getOrNull(15),
                    propertyName7 = columns.getOrNull(16),
                    propertyName8 = columns.getOrNull(17),
                    propertyName9 = columns.getOrNull(18),
                    propertyName10 = columns.getOrNull(19),
                )
            }
            .first()
        cardSetDao.upsertCardSet(cardSet.toEntity())
        cardSetUserDataDao.insertIfAbsent(
            CardSetUserDataEntity(
                id = cardSet.id,
                isActive = true,
                isOwned = true,
            ),
        )
    }

    override suspend fun updateCardSet(cardSet: CardSet) {
        cardSetDao.upsertCardSet(cardSet.toEntity())
        cardSetUserDataDao.upsert(cardSet.toUserDataEntity())
    }

    override suspend fun toggleActiveState(setId: String) {
        cardSetUserDataDao.toggleActiveState(setId)
    }

    override suspend fun getCardSetById(id: String): CardSet? {
        return cardSetDao
            .getCardSetById(id = id)
            ?.toDomain()
    }

    override suspend fun getActiveCardSets(): List<CardSetUserData> {
        return cardSetDao
            .getActiveCardSetsUserData()
            .mapNotNull { it.toUserData() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllSetProgressFlow(): Flow<List<CardSet>> {
        return cardSetDao
            .getAllSetsWithUserData()
            .flatMapLatest { setsWithUserData ->
                if (setsWithUserData.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        setsWithUserData.map { setWithUserData ->
                            collectableCardDao
                                .getCardsWithUserDataByCardSetId(setWithUserData.cardSet.id)
                                .map { cardsWithUserData ->
                                    setWithUserData.toDomain(cardsWithUserData.map { it.toDomain() })
                                }
                        },
                    ) { cardSets -> cardSets.toList() }
                }
            }
    }

    override suspend fun updateUserData(userData: CardSetUserData) {
        cardSetUserDataDao.upsert(userData.toEntity())
    }
}