package com.heveamobile.setsandsteps.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardWithUserData
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectableCardDao {

    @Query("SELECT * FROM CollectableCardEntity WHERE cardSetId = :cardId")
    fun getCardsByCardSetId(cardId: String): List<CollectableCardEntity>

    @Transaction
    @Query("SELECT * FROM CollectableCardEntity WHERE cardSetId = :cardSetId")
    fun getCardsWithUserDataByCardSetId(cardSetId: String): Flow<List<CollectableCardWithUserData>>

    @Transaction
    @Query("SELECT * FROM CollectableCardEntity WHERE id = :id")
    fun getCardById(id: String): CollectableCardWithUserData?

    @Upsert
    suspend fun upsertCard(card: CollectableCardEntity)

    @Upsert
    suspend fun upsertCards(cards: List<CollectableCardEntity>)

    @Query("SELECT id FROM CollectableCardEntity WHERE cardSetId = :cardSetId")
    suspend fun getCardIdsByCardSetId(cardSetId: String): List<String>

    @Query("DELETE FROM CollectableCardEntity WHERE cardSetId = :cardSetId AND id NOT IN (:ids)")
    suspend fun deleteCardsNotIn(
        cardSetId: String,
        ids: List<String>,
    )

//    @Transaction
//    @Query("UPDATE CollectableCardEntity SET isDiscovered = 0 WHERE cardSetId = :cardSetId")
//    fun resetDiscovered(cardSetId: String)
//
//    @Query("UPDATE CollectableCardEntity SET findCount = :count WHERE id = :id")
//    suspend fun updateFindCountForCardById(
//        id: String,
//        count: Int,
//    )
}