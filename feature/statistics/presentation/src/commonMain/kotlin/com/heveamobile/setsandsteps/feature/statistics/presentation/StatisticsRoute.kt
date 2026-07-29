package com.heveamobile.setsandsteps.feature.statistics.presentation

import com.heveamobile.setsandsteps.core.navigation.DrawerRoute
import com.heveamobile.setsandsteps.core.navigation.NavigationDrawerRoute
import com.heveamobile.setsandsteps.core.navigation.Route
import kotlinx.serialization.Serializable
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@Serializable
data object StatisticsRoute : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.Statistics
}

@OptIn(KoinExperimentalAPI::class)
val statisticsPresentationModule = module {
    viewModelOf(::StatisticsViewModel)
    navigation<StatisticsRoute> { StatisticsScreen() }
}
