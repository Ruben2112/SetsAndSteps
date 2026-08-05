package com.heveamobile.setsandsteps.core.data.repository

import com.heveamobile.setsandsteps.core.data.mapper.toDomain
import com.heveamobile.setsandsteps.core.data.mapper.toEntity
import com.heveamobile.setsandsteps.core.data.source.remote.SupabaseCardCatalogDataSource
import com.heveamobile.setsandsteps.core.database.dao.CardSetDao
import com.heveamobile.setsandsteps.core.database.dao.CardSetUserDataDao
import com.heveamobile.setsandsteps.core.database.dao.CollectableCardDao
import com.heveamobile.setsandsteps.core.database.dao.CollectableCardUserDataDao
import com.heveamobile.setsandsteps.core.database.entity.CardSetUserDataEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardUserDataEntity
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.repository.CardSetCatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CardSetCatalogRepositoryImpl(
    private val supabaseCardCatalogDataSource: SupabaseCardCatalogDataSource,
    private val cardSetDao: CardSetDao,
    private val cardSetUserDataDao: CardSetUserDataDao,
    private val collectableCardDao: CollectableCardDao,
    private val collectableCardUserDataDao: CollectableCardUserDataDao,
) : CardSetCatalogRepository {

    private val _remoteCardSets = MutableStateFlow<List<CardSet>>(emptyList())
    override fun getRemoteCardSetsFlow(): Flow<List<CardSet>> = _remoteCardSets.asStateFlow()

    override suspend fun refreshCatalog(): Result<Unit> {
        return runCatching {
            val rarityCountsBySetId = supabaseCardCatalogDataSource
                .getCardRarityCounts()
                .associateBy { it.setId }
            val sets = supabaseCardCatalogDataSource
                .getCardSets()
                .map { setDto ->
                    setDto.toDomain(
                        rarityCounts = rarityCountsBySetId[setDto.id]?.toDomain(),
                    )
                }
            _remoteCardSets.value = sets
        }
    }

    override suspend fun downloadCardSet(setId: String): Result<Unit> {
        return runCatching {
            val remoteSet = _remoteCardSets.value.firstOrNull { it.id == setId }
                ?: supabaseCardCatalogDataSource
                    .getCardSets()
                    .first { it.id == setId }
                    .toDomain(rarityCounts = null)

            // Claim: local-only write, no network call. Flips the set to "owned" immediately,
            // moving it from Catalog to My Sets before the (slower) card content is fetched.
            cardSetDao.upsertCardSet(remoteSet.toEntity())
            cardSetUserDataDao.insertIfAbsent(
                CardSetUserDataEntity(
                    id = setId,
                    isActive = false,
                    isOwned = true,
                ),
            )

            val cards = supabaseCardCatalogDataSource
                .getCards(setId)
                .map { it.toDomain() }
            collectableCardDao.upsertCards(cards.map { it.toEntity() })
            collectableCardUserDataDao.insertIfAbsent(
                cards.map { CollectableCardUserDataEntity(id = it.id) },
            )
        }
    }

    override suspend fun updateCardSet(setId: String): Result<Unit> {
        return runCatching {
            val remoteSet = fetchRemoteSet(setId)

            cardSetDao.upsertCardSet(remoteSet.toEntity())
            collectableCardDao.upsertCards(remoteSet.cards.map { it.toEntity() })
            collectableCardUserDataDao.insertIfAbsent(
                remoteSet.cards.map { CollectableCardUserDataEntity(id = it.id) },
            )

            val remoteCardIds = remoteSet.cards.map { it.id }
            val localCardIds = collectableCardDao.getCardIdsByCardSetId(setId)
            val removedCardIds = localCardIds - remoteCardIds.toSet()

            collectableCardDao.deleteCardsNotIn(
                setId,
                remoteCardIds,
            )
            if (removedCardIds.isNotEmpty()) {
                collectableCardUserDataDao.deleteByIds(removedCardIds)
            }
        }
    }

    private suspend fun fetchRemoteSet(setId: String): CardSet {
        val setDto = supabaseCardCatalogDataSource
            .getCardSets()
            .first { it.id == setId }
        val cards = supabaseCardCatalogDataSource
            .getCards(setId)
            .map { it.toDomain() }
        return setDto.toDomain(cards)
    }
}
