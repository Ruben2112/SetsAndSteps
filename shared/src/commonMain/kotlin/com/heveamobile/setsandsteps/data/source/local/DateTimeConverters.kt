package com.heveamobile.setsandsteps.data.source.local

import androidx.room.TypeConverter
import kotlin.time.Instant

class DateTimeConverters {
    @TypeConverter
    fun timestampToInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun instantToTimestamp(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }
}