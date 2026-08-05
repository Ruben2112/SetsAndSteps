package com.heveamobile.setsandsteps.core.data.di

import com.heveamobile.setsandsteps.core.data.manager.DefaultCardSetDownloadCoordinator
import com.heveamobile.setsandsteps.core.data.repository.CardSetCatalogRepositoryImpl
import com.heveamobile.setsandsteps.core.domain.manager.CardSetDownloadCoordinator
import com.heveamobile.setsandsteps.core.domain.repository.CardSetCatalogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val cardSetCatalogModule = module {
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    singleOf(::CardSetCatalogRepositoryImpl) { bind<CardSetCatalogRepository>() }
    singleOf(::DefaultCardSetDownloadCoordinator) { bind<CardSetDownloadCoordinator>() }
}
