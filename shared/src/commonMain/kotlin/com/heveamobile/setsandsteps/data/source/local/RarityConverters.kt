package com.heveamobile.setsandsteps.data.source.local

import androidx.room.TypeConverter
import com.heveamobile.setsandsteps.domain.model.Rarity

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