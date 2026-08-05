package com.heveamobile.setsandsteps.core.domain.model

data class CardRarityCounts(
    val common: Int,
    val uncommon: Int,
    val rare: Int,
    val epic: Int,
    val legendary: Int,
) {
    val total: Int
        get() = common + uncommon + rare + epic + legendary

    fun count(rarity: Rarity): Int {
        return when (rarity) {
            Rarity.Common -> common
            Rarity.Uncommon -> uncommon
            Rarity.Rare -> rare
            Rarity.Epic -> epic
            Rarity.Legendary -> legendary
        }
    }
}
