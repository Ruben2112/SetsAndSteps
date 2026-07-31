package com.heveamobile.setsandsteps.core.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardSetDto(
    val id: String,
    val name: String,
    val version: Int,
    @SerialName("base_distance")
    val baseDistance: Int,
    @SerialName("common_value")
    val commonValue: Int,
    @SerialName("uncommon_value")
    val uncommonValue: Int,
    @SerialName("rare_value")
    val rareValue: Int,
    @SerialName("epic_value")
    val epicValue: Int,
    @SerialName("legendary_value")
    val legendaryValue: Int,
    @SerialName("backside_image")
    val backsideImage: String? = null,
    @SerialName("property_name1")
    val propertyName1: String? = null,
    @SerialName("property_name2")
    val propertyName2: String? = null,
    @SerialName("property_name3")
    val propertyName3: String? = null,
    @SerialName("property_name4")
    val propertyName4: String? = null,
    @SerialName("property_name5")
    val propertyName5: String? = null,
    @SerialName("property_name6")
    val propertyName6: String? = null,
    @SerialName("property_name7")
    val propertyName7: String? = null,
    @SerialName("property_name8")
    val propertyName8: String? = null,
    @SerialName("property_name9")
    val propertyName9: String? = null,
    @SerialName("property_name10")
    val propertyName10: String? = null,
)
