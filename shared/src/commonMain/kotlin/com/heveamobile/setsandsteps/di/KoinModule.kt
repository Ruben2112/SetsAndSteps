package com.heveamobile.setsandsteps.di

import com.heveamobile.setsandsteps.core.domain.usecase.ExportDatabaseUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.FindCardsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
import com.heveamobile.setsandsteps.core.domain.usecase.GetCountOfCardsInExchangeStockUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetDailyStepsChartDataUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetSetsWithProgressUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetUserUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.ImportDatabaseUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.PurchaseCardsFromExchangeUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.SpendStepsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.SyncStepsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.UpdateUserRecordsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.UpsertInitialMapDataUseCase
import com.heveamobile.setsandsteps.core.foundcards.foundCardsModule
import com.heveamobile.setsandsteps.core.navigation.NavigationHandler
import com.heveamobile.setsandsteps.feature.cards.presentation.cardsPresentationModule
import com.heveamobile.setsandsteps.feature.profile.presentation.profilePresentationModule
import com.heveamobile.setsandsteps.feature.setpointexchange.presentation.setPointExchangePresentationModule
import com.heveamobile.setsandsteps.feature.sets.presentation.setsPresentationModule
import com.heveamobile.setsandsteps.feature.settings.presentation.settingsPresentationModule
import com.heveamobile.setsandsteps.shell.HomeViewModel
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {
    singleOf(::NavigationHandler)
    singleOf(::FoundCardsHandler)
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
}

val useCaseModule = module {
    factoryOf(::GetUserUseCase)
    factoryOf(::SyncStepsUseCase)
    factoryOf(::UpdateUserRecordsUseCase)
    factoryOf(::UpsertInitialMapDataUseCase)
    factoryOf(::GetSetsWithProgressUseCase)
    factoryOf(::FindCardsUseCase)
    factoryOf(::SpendStepsUseCase)
    factoryOf(::GetCountOfCardsInExchangeStockUseCase)
    factoryOf(::PurchaseCardsFromExchangeUseCase)
    factoryOf(::GetDailyStepsChartDataUseCase)
    factoryOf(::ExportDatabaseUseCase)
    factoryOf(::ImportDatabaseUseCase)
}

expect val targetModule: Module

fun getKoinModules() = listOf(
    navigationModule,
    viewModelModule,
    useCaseModule,
    targetModule,
    foundCardsModule,
    settingsPresentationModule,
    profilePresentationModule,
    setsPresentationModule,
    cardsPresentationModule,
    setPointExchangePresentationModule,
)

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(getKoinModules())
    }
}
