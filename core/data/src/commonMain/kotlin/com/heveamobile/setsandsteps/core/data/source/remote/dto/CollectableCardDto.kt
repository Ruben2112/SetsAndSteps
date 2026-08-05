package com.heveamobile.setsandsteps.core.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectableCardDto(
    val id: String,
    @SerialName("set_id")
    val setId: String,
    val name: String,
    val rarity: Int,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val bbox: String? = null,
    @SerialName("property_value1")
    val propertyValue1: String? = null,
    @SerialName("property_value2")
    val propertyValue2: String? = null,
    @SerialName("property_value3")
    val propertyValue3: String? = null,
    @SerialName("property_value4")
    val propertyValue4: String? = null,
    @SerialName("property_value5")
    val propertyValue5: String? = null,
    @SerialName("property_value6")
    val propertyValue6: String? = null,
    @SerialName("property_value7")
    val propertyValue7: String? = null,
    @SerialName("property_value8")
    val propertyValue8: String? = null,
    @SerialName("property_value9")
    val propertyValue9: String? = null,
    @SerialName("property_value10")
    val propertyValue10: String? = null,
)
