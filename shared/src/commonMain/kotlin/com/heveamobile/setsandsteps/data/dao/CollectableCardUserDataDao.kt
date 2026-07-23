package com.heveamobile.setsandsteps.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.heveamobile.setsandsteps.data.entity.CollectableCardUserDataEntity

@Dao
interface CollectableCardUserDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(userData: CollectableCardUserDataEntity)

    @Upsert
    suspend fun upsert(userData: CollectableCardUserDataEntity)

    @Query(
        "UPDATE CollectableCardUserDataEntity SET isDiscovered = 0 " +
                "WHERE id IN (SELECT id FROM CollectableCardEntity WHERE cardSetId = :cardSetId)",
    )
    suspend fun resetDiscovered(cardSetId: String)

    @Query("UPDATE CollectableCardUserDataEntity SET findCount = :count WHERE id = :id")
    suspend fun updateFindCount(
        id: String,
        count: Int,
    )
}
