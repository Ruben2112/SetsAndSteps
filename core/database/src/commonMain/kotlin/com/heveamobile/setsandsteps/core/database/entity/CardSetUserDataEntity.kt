package com.heveamobile.setsandsteps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardSetUserDataEntity(
    @PrimaryKey
    val id: String,
    val isActive: Boolean = false,
    val isOwned: Boolean = false,
    val currentLevel: Int = 1,
    val currentSetPoints: Long = 0,
)