package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.User
import com.heveamobile.setsandsteps.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    // Mutex ensures that only one coroutine can execute the creation logic at a time
    private val mutex = Mutex()

    suspend operator fun invoke(): Flow<User?> {
        ensureUserExists()
        return userRepository.getUserWithStepDataFlow()
    }

    suspend fun getOneShotUser(): User {
        ensureUserExists()
        return userRepository.getUser()!!
    }

    private suspend fun ensureUserExists() {
        if (userRepository.getUser() == null) {
            mutex.withLock {
                // Double-check inside the lock to see if a previous coroutine created it
                if (userRepository.getUser() == null) {
                    userRepository.createUser()
                }
            }
        }
    }

}