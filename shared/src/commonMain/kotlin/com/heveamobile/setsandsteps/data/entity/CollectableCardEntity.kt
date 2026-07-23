package com.heveamobile.setsandsteps.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CardSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardSetId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class CollectableCardEntity(
    @PrimaryKey
    val id: String,
    val cardSetId: String,
    val name: String,
    val rarity: Int,
    val imageUrl: String? = null,
    val bbox: String? = null,
    val propertyValue1: String? = null,
    val propertyValue2: String? = null,
    val propertyValue3: String? = null,
    val propertyValue4: String? = null,
    val propertyValue5: String? = null,
    val propertyValue6: String? = null,
    val propertyValue7: String? = null,
    val propertyValue8: String? = null,
    val propertyValue9: String? = null,
    val propertyValue10: String? = null,
)
