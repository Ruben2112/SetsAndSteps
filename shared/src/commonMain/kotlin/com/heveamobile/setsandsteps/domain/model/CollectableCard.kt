package com.heveamobile.setsandsteps.domain.model

data class CollectableCard(
    val id: String,
    val cardSetId: String,
    val name: String,
    val rarity: Rarity,
    val imageUrl: String? = null,
    val bbox: String?,
    val propertyValue1: String?,
    val propertyValue2: String?,
    val propertyValue3: String?,
    val propertyValue4: String?,
    val propertyValue5: String?,
    val propertyValue6: String?,
    val propertyValue7: String?,
    val propertyValue8: String?,
    val propertyValue9: String?,
    val propertyValue10: String?,
    val userData: CollectableCardUserData? = null,
)