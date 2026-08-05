package com.heveamobile.setsandsteps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.heveamobile.setsandsteps.core.database.entity.CardSetUserDataEntity

@Dao
interface CardSetUserDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(userData: CardSetUserDataEntity)

    @Upsert
    suspend fun upsert(userData: CardSetUserDataEntity)

    @Query("UPDATE CardSetUserDataEntity SET isActive = NOT isActive WHERE id = :id")
    suspend fun toggleActiveState(id: String)
}
