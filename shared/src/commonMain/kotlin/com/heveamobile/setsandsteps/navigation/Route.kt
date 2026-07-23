package com.heveamobile.setsandsteps.navigation

import androidx.navigation3.runtime.NavKey
import com.heveamobile.setsandsteps.ui.home.NavigationDrawerRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Profile : Route {
        override fun toNavigationDrawerRoute(): NavigationDrawerRoute {
            return NavigationDrawerRoute.Profile
        }
    }

    @Serializable
    data object Sets : Route {
        override fun toNavigationDrawerRoute(): NavigationDrawerRoute {
            return NavigationDrawerRoute.Sets
        }
    }

    @Serializable
    data object Destinations : Route {
        override fun toNavigationDrawerRoute(): NavigationDrawerRoute {
            return NavigationDrawerRoute.Cards
        }
    }

    @Serializable
    data class DestinationInfo(
        val destinationId: String?,
    ) : Route {
        override fun toNavigationDrawerRoute(): NavigationDrawerRoute {
            return NavigationDrawerRoute.CardDetails
        }
    }

    @Serializable
    data object SetPointExchange : Route {
        override fun toNavigationDrawerRoute(): NavigationDrawerRoute {
            return NavigationDrawerRoute.SetPointExchange
        }
    }

    @Serializable
    data object Settings : Route {
        override fun toNavigationDrawerRoute(): NavigationDrawerRoute {
            return NavigationDrawerRoute.Settings
        }
    }

    fun toNavigationDrawerRoute(): NavigationDrawerRoute
}