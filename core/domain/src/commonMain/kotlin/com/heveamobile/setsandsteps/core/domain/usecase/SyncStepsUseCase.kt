package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.StepDataRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class SyncStepsUseCase(
    private val stepDataRepository: StepDataRepository,
    private val userRepository: UserRepository,
    private val cardSetRepository: CardSetRepository,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserRecordsUseCase: UpdateUserRecordsUseCase,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {

        val user = getUserUseCase.getOneShotUser()
        val currentTime = Clock.System.now()
        val startTime = user.lastSyncTime
            ?: user.startTime

        val newStepData = stepDataRepository.fetchRemoteSteps(
            startTime,
            currentTime,
        )

        if (newStepData.isNotEmpty()) {
            val newSteps = newStepData.sumOf { it.count }
            stepDataRepository.saveStepData(
                userId = user.id,
                stepData = newStepData,
            )

            userRepository.updateUser(
                user.copy(
                    lastSyncTime = currentTime,
                    totalSteps = user.totalSteps + newStepData.sumOf { it.count },
                ),
            )

            cardSetRepository
                .getActiveCardSets()
                .forEach { cardSetUserData ->
                    cardSetRepository.updateUserData(userData = cardSetUserData.copy(currentSteps = cardSetUserData.currentSteps + newSteps))
                }

            stepDataRepository.deleteOutdatedData(
                before = currentTime - 60.days,
            )
        } else {
            userRepository.updateUser(
                user.copy(lastSyncTime = currentTime),
            )
        }
        updateUserRecordsUseCase()
    }
}