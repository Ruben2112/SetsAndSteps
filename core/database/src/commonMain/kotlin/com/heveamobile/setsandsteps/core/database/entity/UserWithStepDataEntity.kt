package com.heveamobile.setsandsteps.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithStepDataEntity(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
    )
    val stepData: List<StepDataEntity>,
)