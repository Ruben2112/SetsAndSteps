package com.heveamobile.setsandsteps.data.source.remote

import com.heveamobile.setsandsteps.domain.model.StepData
import kotlin.time.Instant

interface HealthDataSource {
    suspend fun fetchSteps(
        startTime: Instant,
        endTime: Instant,
    ): List<StepData>
}