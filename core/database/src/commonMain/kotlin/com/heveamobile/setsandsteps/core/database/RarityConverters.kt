package com.heveamobile.setsandsteps.core.database

import androidx.room.TypeConverter
import com.heveamobile.setsandsteps.core.domain.model.Rarity

class RarityConverters {
    @TypeConverter
    fun intToRarity(value: Int): Rarity {
        return Rarity.fromInt(value)
    }

    @TypeConverter
    fun rarityToInt(rarity: Rarity): Int {
        return rarity.intValue
    }
}