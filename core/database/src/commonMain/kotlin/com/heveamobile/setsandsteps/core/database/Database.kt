package com.heveamobile.setsandsteps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.heveamobile.setsandsteps.core.database.dao.CardSetDao
import com.heveamobile.setsandsteps.core.database.dao.CardSetUserDataDao
import com.heveamobile.setsandsteps.core.database.dao.CollectableCardDao
import com.heveamobile.setsandsteps.core.database.dao.CollectableCardUserDataDao
import com.heveamobile.setsandsteps.core.database.dao.StepDataDao
import com.heveamobile.setsandsteps.core.database.dao.UserDao
import com.heveamobile.setsandsteps.core.database.entity.CardSetEntity
import com.heveamobile.setsandsteps.core.database.entity.CardSetUserDataEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardEntity
import com.heveamobile.setsandsteps.core.database.entity.CollectableCardUserDataEntity
import com.heveamobile.setsandsteps.core.database.entity.StepDataEntity
import com.heveamobile.setsandsteps.core.database.entity.UserEntity

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