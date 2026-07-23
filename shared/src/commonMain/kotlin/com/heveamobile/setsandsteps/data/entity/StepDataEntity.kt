package com.heveamobile.setsandsteps.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity
data class StepDataEntity(
    val userId: Long,
    @PrimaryKey
    val startTime: Instant,
    val endTime: Instant,
    val count: Long,
)