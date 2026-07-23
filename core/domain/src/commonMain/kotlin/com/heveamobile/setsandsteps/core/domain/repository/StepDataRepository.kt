package com.heveamobile.setsandsteps.core.domain.repository

import com.heveamobile.setsandsteps.core.domain.model.StepData
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