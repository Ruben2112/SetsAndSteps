package com.heveamobile.setsandsteps.core.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardRarityCountDto(
    @SerialName("set_id")
    val setId: String,
    @SerialName("common_count")
    val commonCount: Int,
    @SerialName("uncommon_count")
    val uncommonCount: Int,
    @SerialName("rare_count")
    val rareCount: Int,
    @SerialName("epic_count")
    val epicCount: Int,
    @SerialName("legendary_count")
    val legendaryCount: Int,
)
