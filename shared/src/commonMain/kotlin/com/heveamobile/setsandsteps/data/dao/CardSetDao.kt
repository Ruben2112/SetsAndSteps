package com.heveamobile.setsandsteps.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.heveamobile.setsandsteps.data.entity.CardSetEntity
import com.heveamobile.setsandsteps.data.entity.CardSetWithUserData
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSetDao {

    @Transaction
    @Query("SELECT * FROM CardSetEntity")
    fun getAllSetsWithUserData(): Flow<List<CardSetWithUserData>>

    @Query("SELECT * FROM CardSetEntity WHERE id = :id")
    suspend fun getCardSetById(id: String): CardSetEntity?

    @Upsert
    suspend fun upsertCardSet(cardSet: CardSetEntity)
}