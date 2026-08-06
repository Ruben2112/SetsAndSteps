package com.heveamobile.setsandsteps.core.domain.model

import kotlin.time.Instant

data class User(
    val id: Long,
    val startTime: Instant,
    val lastSyncTime: Instant? = null,
    val totalSteps: Long = 0,
    val previousTwentyFourHours: Long = 0,
    val twentyFourHourRecord: Long = 0,
    val previousSevenDays: Long = 0,
    val sevenDayRecord: Long = 0,
    val previousThirtyDays: Long = 0,
    val thirtyDayRecord: Long = 0,
    val stepData: List<StepData> = emptyList(),
)
