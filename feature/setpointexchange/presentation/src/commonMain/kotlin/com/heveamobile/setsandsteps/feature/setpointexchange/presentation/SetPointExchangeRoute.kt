package com.heveamobile.setsandsteps.feature.setpointexchange.presentation

import com.heveamobile.setsandsteps.core.navigation.DrawerRoute
import com.heveamobile.setsandsteps.core.navigation.NavigationDrawerRoute
import com.heveamobile.setsandsteps.core.navigation.Route
import kotlinx.serialization.Serializable
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@Serializable
data object SetPointExchangeRoute : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.SetPointExchange
}

@OptIn(KoinExperimentalAPI::class)
val setPointExchangePresentationModule = module {
    viewModelOf(::SetPointExchangeViewModel)
    navigation<SetPointExchangeRoute> { SetPointExchangeScreen() }
}
