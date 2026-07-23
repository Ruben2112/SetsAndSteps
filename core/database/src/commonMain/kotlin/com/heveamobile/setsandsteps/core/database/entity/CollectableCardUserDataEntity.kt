package com.heveamobile.setsandsteps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CollectableCardUserDataEntity(
    @PrimaryKey
    val id: String,
    val isDiscovered: Boolean = false,
    val findCount: Int = 0,
)
