package com.heveamobile.setsandsteps.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CardSetWithUserData(
    @Embedded
    val cardSet: CardSetEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val userData: CardSetUserDataEntity?,
)