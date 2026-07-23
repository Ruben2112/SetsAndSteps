package com.heveamobile.setsandsteps.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.heveamobile.setsandsteps.data.dao.CardSetDao
import com.heveamobile.setsandsteps.data.dao.CardSetUserDataDao
import com.heveamobile.setsandsteps.data.dao.CollectableCardDao
import com.heveamobile.setsandsteps.data.dao.CollectableCardUserDataDao
import com.heveamobile.setsandsteps.data.dao.StepDataDao
import com.heveamobile.setsandsteps.data.dao.UserDao
import com.heveamobile.setsandsteps.data.entity.CardSetEntity
import com.heveamobile.setsandsteps.data.entity.CardSetUserDataEntity
import com.heveamobile.setsandsteps.data.entity.CollectableCardEntity
import com.heveamobile.setsandsteps.data.entity.CollectableCardUserDataEntity
import com.heveamobile.setsandsteps.data.entity.StepDataEntity
import com.heveamobile.setsandsteps.data.entity.UserEntity

const val DATABASE_FILE_NAME = "map_by_step.db"

@Database(
    entities = [
        StepDataEntity::class,
        UserEntity::class,
        CardSetEntity::class,
        CardSetUserDataEntity::class,
        CollectableCardEntity::class,
        CollectableCardUserDataEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    DateTimeConverters::class,
    RarityConverters::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getStepDataDao(): StepDataDao
    abstract fun getUserDao(): UserDao
    abstract fun getCardSetDao(): CardSetDao
    abstract fun getCardSetUserDataDao(): CardSetUserDataDao
    abstract fun getCardDao(): CollectableCardDao
    abstract fun getCardUserDataDao(): CollectableCardUserDataDao
}