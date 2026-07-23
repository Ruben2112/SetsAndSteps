package com.heveamobile.setsandsteps.di

import com.heveamobile.setsandsteps.data.repository.CardSetRepositoryImpl
import com.heveamobile.setsandsteps.data.repository.CollectableCardRepositoryImpl
import com.heveamobile.setsandsteps.data.repository.StepDataRepositoryImpl
import com.heveamobile.setsandsteps.data.repository.UserPreferencesRepositoryImpl
import com.heveamobile.setsandsteps.data.repository.UserRepositoryImpl
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.CollectableCardRepository
import com.heveamobile.setsandsteps.core.domain.repository.StepDataRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserRepository
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
import com.heveamobile.setsandsteps.navigation.NavigationHandler
import com.heveamobile.setsandsteps.navigation.Route
import com.heveamobile.setsandsteps.ui.carddetails.CardDetailsViewModel
import com.heveamobile.setsandsteps.ui.carddetails.DestinationInfoScreen
import com.heveamobile.setsandsteps.ui.cards.CardsViewModel
import com.heveamobile.setsandsteps.ui.cards.DestinationsScreen
import com.heveamobile.setsandsteps.ui.home.HomeViewModel
import com.heveamobile.setsandsteps.ui.profile.ProfileScreen
import com.heveamobile.setsandsteps.ui.profile.ProfileViewModel
import com.heveamobile.setsandsteps.ui.setpointexchange.SetPointExchangeScreen
import com.heveamobile.setsandsteps.ui.setpointexchange.SetPointExchangeViewModel
import com.heveamobile.setsandsteps.ui.sets.SetsScreen
import com.heveamobile.setsandsteps.ui.sets.SetsViewModel
import com.heveamobile.setsandsteps.ui.settings.SettingsScreen
import com.heveamobile.setsandsteps.ui.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {
    singleOf(::NavigationHandler)
    singleOf(::FoundCardsHandler)
    navigation<Route.Profile> { ProfileScreen() }
    navigation<Route.Sets> { SetsScreen() }
    navigation<Route.Destinations> { DestinationsScreen() }
    navigation<Route.DestinationInfo> { route ->
        DestinationInfoScreen(
            viewModel = koinViewModel { parametersOf(route) },
            route = route,
        )
    }
    navigation<Route.SetPointExchange> { SetPointExchangeScreen() }
    navigation<Route.Settings> { SettingsScreen() }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SetsViewModel)
    viewModelOf(::CardsViewModel)
    viewModel { _ ->
        CardDetailsViewModel(
            collectableCardRepository = get(),
            getSetsWithProgressUseCase = get(),
        )
    }
    viewModelOf(::SetPointExchangeViewModel)
    viewModelOf(::SettingsViewModel)
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

val repositoryModule = module {
    singleOf(::StepDataRepositoryImpl) { bind<StepDataRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::CardSetRepositoryImpl) { bind<CardSetRepository>() }
    singleOf(::CollectableCardRepositoryImpl) { bind<CollectableCardRepository>() }
    singleOf(::UserPreferencesRepositoryImpl) { bind<UserPreferencesRepository>() }
}

expect val targetModule: Module

fun getKoinModules() = listOf(
    navigationModule,
    viewModelModule,
    useCaseModule,
    repositoryModule,
    targetModule,
)

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(getKoinModules())
    }
}
