package com.heveamobile.setsandsteps.core.data.mapper

import com.heveamobile.setsandsteps.core.data.source.remote.dto.CardRarityCountDto
import com.heveamobile.setsandsteps.core.data.source.remote.dto.CardSetDto
import com.heveamobile.setsandsteps.core.data.source.remote.dto.CollectableCardDto
import com.heveamobile.setsandsteps.core.domain.model.CardRarityCounts
import com.heveamobile.setsandsteps.core.domain.model.CardSet
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.Rarity

fun CollectableCardDto.toDomain(): CollectableCard {
    return CollectableCard(
        id = this.id,
        cardSetId = this.setId,
        name = this.name,
        rarity = Rarity.fromInt(this.rarity),
        imageUrl = this.imageUrl,
        bbox = this.bbox,
        propertyValue1 = this.propertyValue1,
        propertyValue2 = this.propertyValue2,
        propertyValue3 = this.propertyValue3,
        propertyValue4 = this.propertyValue4,
        propertyValue5 = this.propertyValue5,
        propertyValue6 = this.propertyValue6,
        propertyValue7 = this.propertyValue7,
        propertyValue8 = this.propertyValue8,
        propertyValue9 = this.propertyValue9,
        propertyValue10 = this.propertyValue10,
    )
}

fun CardSetDto.toDomain(cards: List<CollectableCard>): CardSet {
    return toDomain(
        cards = cards,
        rarityCounts = null,
    )
}

fun CardSetDto.toDomain(rarityCounts: CardRarityCounts?): CardSet {
    return toDomain(
        cards = emptyList(),
        rarityCounts = rarityCounts,
    )
}

private fun CardSetDto.toDomain(
    cards: List<CollectableCard>,
    rarityCounts: CardRarityCounts?,
): CardSet {
    return CardSet(
        id = this.id,
        name = this.name,
        version = this.version,
        baseDistance = this.baseDistance.toLong(),
        commonValue = this.commonValue,
        uncommonValue = this.uncommonValue,
        rareValue = this.rareValue,
        epicValue = this.epicValue,
        legendaryValue = this.legendaryValue,
        backsideImageUrl = this.backsideImage,
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
        cards = cards,
        rarityCounts = rarityCounts,
    )
}

fun CardRarityCountDto.toDomain(): CardRarityCounts {
    return CardRarityCounts(
        common = this.commonCount,
        uncommon = this.uncommonCount,
        rare = this.rareCount,
        epic = this.epicCount,
        legendary = this.legendaryCount,
    )
}
