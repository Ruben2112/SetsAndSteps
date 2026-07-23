package com.heveamobile.setsandsteps.ui.home

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.ic_card_details
import setsandsteps.shared.generated.resources.ic_cards
import setsandsteps.shared.generated.resources.ic_profile
import setsandsteps.shared.generated.resources.ic_set_point_exchange
import setsandsteps.shared.generated.resources.ic_sets
import setsandsteps.shared.generated.resources.ic_settings
import setsandsteps.shared.generated.resources.navigation_route_card_details
import setsandsteps.shared.generated.resources.navigation_route_cards
import setsandsteps.shared.generated.resources.navigation_route_profile
import setsandsteps.shared.generated.resources.navigation_route_set_point_exchange
import setsandsteps.shared.generated.resources.navigation_route_sets
import setsandsteps.shared.generated.resources.navigation_route_settings

enum class NavigationDrawerRoute(
    val routeName: StringResource,
    val icon: DrawableResource,
) {
    Profile(
        Res.string.navigation_route_profile,
        Res.drawable.ic_profile,
    ),
    Sets(
        Res.string.navigation_route_sets,
        Res.drawable.ic_sets,
    ),
    Cards(
        Res.string.navigation_route_cards,
        Res.drawable.ic_cards,
    ),
    CardDetails(
        Res.string.navigation_route_card_details,
        Res.drawable.ic_card_details,
    ),
    SetPointExchange(
        Res.string.navigation_route_set_point_exchange,
        Res.drawable.ic_set_point_exchange,
    ),
    Settings(
        Res.string.navigation_route_settings,
        Res.drawable.ic_settings,
    )
}