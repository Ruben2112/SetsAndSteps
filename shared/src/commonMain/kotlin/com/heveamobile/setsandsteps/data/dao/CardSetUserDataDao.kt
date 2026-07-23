package com.heveamobile.setsandsteps.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert
import com.heveamobile.setsandsteps.data.entity.CardSetUserDataEntity

@Dao
interface CardSetUserDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(userData: CardSetUserDataEntity)

    @Upsert
    suspend fun upsert(userData: CardSetUserDataEntity)
}
