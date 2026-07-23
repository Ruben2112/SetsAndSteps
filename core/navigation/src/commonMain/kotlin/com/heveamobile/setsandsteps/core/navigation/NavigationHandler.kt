package com.heveamobile.setsandsteps.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationHandler {
    private val _navigationEvents = MutableSharedFlow<Route>(extraBufferCapacity = 1)
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun navigateTo(route: Route) {
        _navigationEvents.tryEmit(route)
    }
}
