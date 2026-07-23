package com.heveamobile.setsandsteps.core.data.mapper

import com.heveamobile.setsandsteps.core.database.entity.UserWithStepDataEntity
import com.heveamobile.setsandsteps.core.domain.model.User

fun UserWithStepDataEntity.toDomain(): User {
    return User(
        id = this.user.id,
        startTime = this.user.startTime,
        lastSyncTime = this.user.lastSyncTime,
        availableSteps = this.user.availableSteps,
        totalSteps = this.user.totalSteps,
        previousTwentyFourHours = this.user.previousTwentyFourHours,
        twentyFourHourRecord = this.user.twentyFourHourRecord,
        previousSevenDays = this.user.previousSevenDays,
        sevenDayRecord = this.user.sevenDayRecord,
        previousThirtyDays = this.user.previousThirtyDays,
        thirtyDayRecord = this.user.thirtyDayRecord,
        stepData = this.stepData.map { it.toDomain() },
    )
}