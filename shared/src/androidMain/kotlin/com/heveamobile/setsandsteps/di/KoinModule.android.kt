package com.heveamobile.setsandsteps.di

import com.heveamobile.setsandsteps.core.data.di.coreDataModule
import com.heveamobile.setsandsteps.core.database.di.coreDatabaseModule
import org.koin.dsl.module

actual val targetModule = module {
    includes(coreDatabaseModule, coreDataModule)
}
