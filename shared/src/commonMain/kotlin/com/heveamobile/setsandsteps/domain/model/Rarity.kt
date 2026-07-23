package com.heveamobile.setsandsteps.domain.model

enum class Rarity(
    val intValue: Int,
) {
    Common(1),
    Uncommon(2),
    Rare(3),
    Epic(4),
    Legendary(5);

    companion object {
        fun fromInt(value: Int): Rarity {
            return entries.find { it.intValue == value }
                ?: Common
        }
    }
}