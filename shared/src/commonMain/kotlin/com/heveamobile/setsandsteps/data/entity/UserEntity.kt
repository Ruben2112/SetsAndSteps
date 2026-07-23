package com.heveamobile.setsandsteps.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity
data class UserEntity(
    @PrimaryKey
    val id: Long = 0L,
    val startTime: Instant,
    val lastSyncTime: Instant? = null,
    val availableSteps: Long = 0,
    val totalSteps: Long = 0,
    val previousTwentyFourHours: Long = 0,
    val twentyFourHourRecord: Long = 0,
    val previousSevenDays: Long = 0,
    val sevenDayRecord: Long = 0,
    val previousThirtyDays: Long = 0,
    val thirtyDayRecord: Long = 0,
)
