package com.heveamobile.setsandsteps.core.domain.source

import com.heveamobile.setsandsteps.core.domain.model.StepData
import kotlin.time.Instant

interface HealthDataSource {
    suspend fun fetchSteps(
        startTime: Instant,
        endTime: Instant,
    ): List<StepData>
}