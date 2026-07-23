package com.heveamobile.setsandsteps.data.mapper

import com.heveamobile.setsandsteps.data.entity.StepDataEntity
import com.heveamobile.setsandsteps.domain.model.StepData

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