package com.heveamobile.setsandsteps.domain.model

data class CardSet(
    val id: String,
    val name: String,
    val version: Int,
    val baseDistance: Long,
    val commonValue: Int,
    val uncommonValue: Int,
    val rareValue: Int,
    val epicValue: Int,
    val legendaryValue: Int,
    val backsideImageUrl: String? = null,
    val propertyName1: String? = null,
    val propertyName2: String? = null,
    val propertyName3: String? = null,
    val propertyName4: String? = null,
    val propertyName5: String? = null,
    val propertyName6: String? = null,
    val propertyName7: String? = null,
    val propertyName8: String? = null,
    val propertyName9: String? = null,
    val propertyName10: String? = null,
    val cards: List<CollectableCard> = emptyList(),
    val userData: CardSetUserData? = null,
) {

    fun storePrice(rarity: Rarity): Int {
        val baseValue = when (rarity) {
            Rarity.Common -> commonValue
            Rarity.Uncommon -> uncommonValue
            Rarity.Rare -> rareValue
            Rarity.Epic -> epicValue
            Rarity.Legendary -> legendaryValue
        }
        return baseValue * 10
    }
}