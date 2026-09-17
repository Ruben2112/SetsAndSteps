package com.heveamobile.setsandsteps.core.domain.model

enum class Rarity(
    val intValue: Int,
    val vibrationAmplitude: Int,
) {
    Common(
        1,
        0,
    ),
    Uncommon(
        2,
        40,
    ),
    Rare(
        3,
        80,
    ),
    Epic(
        4,
        160,
    ),
    Legendary(
        5,
        255,
    );

    companion object {
        fun fromInt(value: Int): Rarity {
            return entries.find { it.intValue == value }
                ?: Common
        }
    }
}