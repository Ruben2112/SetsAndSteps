package com.heveamobile.setsandsteps.core.domain.model

import kotlin.time.Instant

data class StepData(
    val startTime: Instant,
    val endTime: Instant,
    val count: Long,
)