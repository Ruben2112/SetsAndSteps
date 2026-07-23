package com.heveamobile.setsandsteps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardSetEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val version: Int,
    val baseDistance: Long,
    val commonValue: Int,
    val uncommonValue: Int,
    val rareValue: Int,
    val epicValue: Int,
    val legendaryValue: Int,
    val backsideImageUrl: String?,
    val propertyName1: String?,
    val propertyName2: String?,
    val propertyName3: String?,
    val propertyName4: String?,
    val propertyName5: String?,
    val propertyName6: String?,
    val propertyName7: String?,
    val propertyName8: String?,
    val propertyName9: String?,
    val propertyName10: String?,
)