package com.heveamobile.setsandsteps.data.mapper

import com.heveamobile.setsandsteps.core.database.entity.CardSetEntity
import com.heveamobile.setsandsteps.core.database.entity.CardSetUserDataEntity
import com.heveamobile.setsandsteps.core.database.entity.CardSetWithUserData
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CardSetUserData
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.Rarity

fun CardSetEntity.toDomain(): CardSet {
    return CardSet(
        id = this.id,
        version = this.version,
        name = this.name,
        baseDistance = this.baseDistance,
        commonValue = this.commonValue,
        uncommonValue = this.uncommonValue,
        rareValue = this.rareValue,
        epicValue = this.epicValue,
        legendaryValue = this.legendaryValue,
        backsideImageUrl = this.backsideImageUrl,
        propertyName1 = this.propertyName1,
        propertyName2 = this.propertyName2,
        propertyName3 = this.propertyName3,
        propertyName4 = this.propertyName4,
        propertyName5 = this.propertyName5,
        propertyName6 = this.propertyName6,
        propertyName7 = this.propertyName7,
        propertyName8 = this.propertyName8,
        propertyName9 = this.propertyName9,
        propertyName10 = this.propertyName10,
    )
}

fun CardSetWithUserData.toDomain(cards: List<CollectableCard>): CardSet {
    val entityUserData = this.userData
    val userData = if (entityUserData == null) null else CardSetUserData(
        isOwned = entityUserData.isOwned,
        isActive = entityUserData.isActive,
        currentLevel = entityUserData.currentLevel,
        calculatedDistance = run {
            val level = entityUserData.currentLevel
            this.cardSet.baseDistance + (this.cardSet.baseDistance * (level - 1) * level) / 20
        },
        currentSetPoints = entityUserData.currentSetPoints,
        totalCardCount = cards.size,
        totalCardsFound = cards.count { it.userData?.isDiscovered == true },
        commonCardCount = cards.count { it.rarity == Rarity.Common },
        commonCardsFound = cards.count { it.rarity == Rarity.Common && it.userData?.isDiscovered == true },
        uncommonCardCount = cards.count { it.rarity == Rarity.Uncommon },
        uncommonCardsFound = cards.count { it.rarity == Rarity.Uncommon && it.userData?.isDiscovered == true },
        rareCardCount = cards.count { it.rarity == Rarity.Rare },
        rareCardsFound = cards.count { it.rarity == Rarity.Rare && it.userData?.isDiscovered == true },
        epicCardCount = cards.count { it.rarity == Rarity.Epic },
        epicCardsFound = cards.count { it.rarity == Rarity.Epic && it.userData?.isDiscovered == true },
        legendaryCardCount = cards.count { it.rarity == Rarity.Legendary },
        legendaryCardsFound = cards.count { it.rarity == Rarity.Legendary && it.userData?.isDiscovered == true },
    )
    return this.cardSet
        .toDomain()
        .copy(
            cards = cards,
            userData = userData,
        )
}

fun CardSet.toUserDataEntity(): CardSetUserDataEntity {
    val userData = this.userData
        ?: CardSetUserData()
    return CardSetUserDataEntity(
        id = this.id,
        isActive = userData.isActive,
        isOwned = userData.isOwned,
        currentLevel = userData.currentLevel,
        currentSetPoints = userData.currentSetPoints,
    )
}

fun CardSet.toEntity(): CardSetEntity {
    return CardSetEntity(
        id = this.id,
        name = this.name,
        version = this.version,
        baseDistance = this.baseDistance,
        commonValue = this.commonValue,
        uncommonValue = this.uncommonValue,
        rareValue = this.rareValue,
        epicValue = this.epicValue,
        legendaryValue = this.legendaryValue,
        backsideImageUrl = this.backsideImageUrl,
        propertyName1 = this.propertyName1,
        propertyName2 = this.propertyName2,
        propertyName3 = this.propertyName3,
        propertyName4 = this.propertyName4,
        propertyName5 = this.propertyName5,
        propertyName6 = this.propertyName6,
        propertyName7 = this.propertyName7,
        propertyName8 = this.propertyName8,
        propertyName9 = this.propertyName9,
        propertyName10 = this.propertyName10,
    )
}