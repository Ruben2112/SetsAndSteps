package com.heveamobile.setsandsteps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.heveamobile.setsandsteps.core.database.entity.UserEntity
import com.heveamobile.setsandsteps.core.database.entity.UserWithStepDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM UserEntity LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Transaction
    @Query("SELECT * FROM UserEntity LIMIT 1")
    suspend fun getUserWithStepData(): UserWithStepDataEntity?

    @Transaction
    @Query("SELECT * FROM UserEntity LIMIT 1")
    fun getUserWithStepDataFlow(): Flow<UserWithStepDataEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)
}