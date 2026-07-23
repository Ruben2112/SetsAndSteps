package com.heveamobile.setsandsteps.navigation

import com.heveamobile.setsandsteps.core.navigation.DrawerRoute
import com.heveamobile.setsandsteps.core.navigation.NavigationDrawerRoute
import com.heveamobile.setsandsteps.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object Profile : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.Profile
}

@Serializable
data object Sets : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.Sets
}

@Serializable
data object Destinations : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.Cards
}

@Serializable
data class DestinationInfo(
    val destinationId: String?,
) : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.CardDetails
}

@Serializable
data object SetPointExchange : Route, DrawerRoute {
    override val navigationDrawerRoute = NavigationDrawerRoute.SetPointExchange
}
