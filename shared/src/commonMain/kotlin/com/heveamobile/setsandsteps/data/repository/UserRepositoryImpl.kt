package com.heveamobile.setsandsteps.data.repository

import com.heveamobile.setsandsteps.data.dao.UserDao
import com.heveamobile.setsandsteps.data.entity.UserEntity
import com.heveamobile.setsandsteps.data.mapper.toDomain
import com.heveamobile.setsandsteps.data.mapper.toEntity
import com.heveamobile.setsandsteps.domain.model.User
import com.heveamobile.setsandsteps.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {
    override suspend fun getUser(): User? {
        return userDao
            .getUser()
            ?.toDomain()
    }

    override fun getUserFlow(): Flow<User?> {
        return userDao
            .getUserFlow()
            .map { it?.toDomain() }
    }

    override suspend fun getUserWithStepData(): User? {
        return userDao
            .getUserWithStepData()
            ?.toDomain()
    }

    override fun getUserWithStepDataFlow(): Flow<User?> {
        return userDao
            .getUserWithStepDataFlow()
            .map { it?.toDomain() }
    }

    override suspend fun createUser() {
        userDao.upsertUser(UserEntity(startTime = Clock.System.now()))
    }

    override suspend fun updateUser(
        user: User,
    ) {
        userDao.upsertUser(
            user.toEntity(),
        )
    }
}