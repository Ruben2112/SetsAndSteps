package com.heveamobile.setsandsteps.feature.cards.presentation

import com.heveamobile.setsandsteps.core.navigation.DrawerRoute
import com.heveamobile.setsandsteps.core.navigation.NavigationDrawerRoute
import com.heveamobile.setsandsteps.core.navigation.Route
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@Serializable
data object Cards : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.Cards
}

@Serializable
data class CardDetails(
    val destinationId: String?,
) : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.CardDetails
}

@OptIn(KoinExperimentalAPI::class)
val cardsPresentationModule = module {
    viewModelOf(::CardsViewModel)
    viewModel { _ ->
        CardDetailsViewModel(
            collectableCardRepository = get(),
            getSetsWithProgressUseCase = get(),
        )
    }
    navigation<Cards> { DestinationsScreen() }
    navigation<CardDetails> { route ->
        DestinationInfoScreen(
            viewModel = koinViewModel { parametersOf(route) },
            route = route,
        )
    }
}
