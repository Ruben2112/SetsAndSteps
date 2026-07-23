package com.heveamobile.setsandsteps.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CollectableCardWithUserData(
    @Embedded
    val card: CollectableCardEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val userData: CollectableCardUserDataEntity?,
)
