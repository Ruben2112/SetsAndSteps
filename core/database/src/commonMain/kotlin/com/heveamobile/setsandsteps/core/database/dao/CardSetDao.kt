package com.heveamobile.setsandsteps.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.heveamobile.setsandsteps.core.database.entity.CardSetEntity
import com.heveamobile.setsandsteps.core.database.entity.CardSetWithUserData
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSetDao {

    @Transaction
    @Query("SELECT * FROM CardSetEntity")
    fun getAllSetsWithUserData(): Flow<List<CardSetWithUserData>>

    @Transaction
    @Query("SELECT * FROM CardSetEntity WHERE id = :id")
    suspend fun getCardSetWithUserDataById(id: String): CardSetWithUserData?

    @Upsert
    suspend fun upsertCardSet(cardSet: CardSetEntity)

    @Transaction
    @Query(
        """
        SELECT CardSetEntity.* FROM CardSetEntity
        INNER JOIN CardSetUserDataEntity ON CardSetEntity.id = CardSetUserDataEntity.id
        WHERE CardSetUserDataEntity.isActive = 1
        """,
    )
    fun getActiveCardSetsUserData(): List<CardSetWithUserData>
}