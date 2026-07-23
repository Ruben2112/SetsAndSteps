package com.heveamobile.setsandsteps.data.mapper

import com.heveamobile.setsandsteps.core.database.entity.CollectableCardEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardUserDataEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardWithUserData
import com.heveamobile.setsandsteps.core.domain.model.CollectableCard
import com.heveamobile.setsandsteps.core.domain.model.CollectableCardUserData
import com.heveamobile.setsandsteps.core.domain.model.Rarity

fun CollectableCardEntity.toDomain(): CollectableCard {
    return CollectableCard(
        id = this.id,
        cardSetId = this.cardSetId,
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

fun CollectableCardWithUserData.toDomain(): CollectableCard {
    val entityUserData = this.userData
    return this.card
        .toDomain()
        .copy(
            userData = if (entityUserData == null) null else CollectableCardUserData(
                isDiscovered = entityUserData.isDiscovered,
                findCount = entityUserData.findCount,
            ),
        )
}

fun CollectableCard.toUserDataEntity(): CollectableCardUserDataEntity {
    val userData = this.userData
        ?: CollectableCardUserData()
    return CollectableCardUserDataEntity(
        id = this.id,
        isDiscovered = userData.isDiscovered,
        findCount = userData.findCount,
    )
}

fun CollectableCard.toEntity(): CollectableCardEntity {
    return CollectableCardEntity(
        id = this.id,
        cardSetId = this.cardSetId,
        name = this.name,
        rarity = this.rarity.intValue,
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