package com.heveamobile.setsandsteps.core.database.di

import com.heveamobile.setsandsteps.core.database.AppDatabase
import com.heveamobile.setsandsteps.core.database.getDatabaseBuilder
import org.koin.dsl.module
import org.koin.dsl.onClose

val coreDatabaseModule = module {
    single { getDatabaseBuilder(get()).build() } onClose { it?.close() }

    single { get<AppDatabase>().getUserDao() }
    single { get<AppDatabase>().getStepDataDao() }
    single { get<AppDatabase>().getCardSetDao() }
    single { get<AppDatabase>().getCardSetUserDataDao() }
    single { get<AppDatabase>().getCardDao() }
    single { get<AppDatabase>().getCardUserDataDao() }
}
