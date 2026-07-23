package com.heveamobile.setsandsteps.core.domain.repository

import com.heveamobile.setsandsteps.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): User?
    fun getUserFlow(): Flow<User?>
    suspend fun getUserWithStepData(): User?
    fun getUserWithStepDataFlow(): Flow<User?>
    suspend fun createUser()
    suspend fun updateUser(user: User)
}