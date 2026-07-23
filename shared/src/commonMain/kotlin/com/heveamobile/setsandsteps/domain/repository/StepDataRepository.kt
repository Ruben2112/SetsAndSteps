package com.heveamobile.setsandsteps.domain.repository

import com.heveamobile.setsandsteps.domain.model.StepData
import kotlin.time.Instant

interface StepDataRepository {
    suspend fun saveStepData(
        userId: Long,
        stepData: List<StepData>,
    )

    suspend fun fetchRemoteSteps(
        startTime: Instant,
        endTime: Instant,
    ): List<StepData>

    suspend fun deleteOutdatedData(
        before: Instant,
    )
}