package com.heveamobile.setsandsteps.core.data.mapper

import com.heveamobile.setsandsteps.core.database.entity.UserEntity
import com.heveamobile.setsandsteps.core.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        startTime = this.startTime,
        lastSyncTime = this.lastSyncTime,
        availableSteps = this.availableSteps,
        totalSteps = this.totalSteps,
        previousTwentyFourHours = this.previousTwentyFourHours,
        twentyFourHourRecord = this.twentyFourHourRecord,
        previousSevenDays = this.previousSevenDays,
        sevenDayRecord = this.sevenDayRecord,
        previousThirtyDays = this.previousThirtyDays,
        thirtyDayRecord = this.thirtyDayRecord,
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        startTime = this.startTime,
        lastSyncTime = this.lastSyncTime,
        availableSteps = this.availableSteps,
        totalSteps = this.totalSteps,
        previousTwentyFourHours = this.previousTwentyFourHours,
        twentyFourHourRecord = this.twentyFourHourRecord,
        previousSevenDays = this.previousSevenDays,
        sevenDayRecord = this.sevenDayRecord,
        previousThirtyDays = this.previousThirtyDays,
        thirtyDayRecord = this.thirtyDayRecord,

    )
}