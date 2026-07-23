package com.heveamobile.setsandsteps.core.data.mapper

import com.heveamobile.setsandsteps.core.database.entity.StepDataEntity
import com.heveamobile.setsandsteps.core.domain.model.StepData

fun StepDataEntity.toDomain(): StepData {
    return StepData(
        count = this.count,
        startTime = this.startTime,
        endTime = this.endTime,
    )
}

fun StepData.toEntity(userId: Long): StepDataEntity {
    return StepDataEntity(
        count = this.count,
        startTime = this.startTime,
        endTime = this.endTime,
        userId = userId,
    )
}