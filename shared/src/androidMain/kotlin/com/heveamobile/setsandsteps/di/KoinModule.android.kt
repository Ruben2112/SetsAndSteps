package com.heveamobile.setsandsteps.di

import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.heveamobile.setsandsteps.core.data.di.coreDataModule
import com.heveamobile.setsandsteps.core.database.di.coreDatabaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    includes(coreDatabaseModule, coreDataModule)

    single<ImageLoader> { SingletonImageLoader.get(androidContext()) }
}
