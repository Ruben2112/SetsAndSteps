package com.heveamobile.setsandsteps.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.test.ic_set_point_exchange
import com.example.test.ic_sets
import com.example.test.ic_settings
import com.heveamobile.setsandsteps.core.navigation.generated.resources.Res
import com.heveamobile.setsandsteps.core.navigation.generated.resources.navigation_route_card_details
import com.heveamobile.setsandsteps.core.navigation.generated.resources.navigation_route_cards
import com.heveamobile.setsandsteps.core.navigation.generated.resources.navigation_route_set_point_exchange
import com.heveamobile.setsandsteps.core.navigation.generated.resources.navigation_route_sets
import com.heveamobile.setsandsteps.core.navigation.generated.resources.navigation_route_settings
import com.heveamobile.setsandsteps.core.navigation.generated.resources.navigation_route_statistics
import com.heveamobile.setsandsteps.core.navigation.icons.ic_card_details
import com.heveamobile.setsandsteps.core.navigation.icons.ic_cards
import com.heveamobile.setsandsteps.core.navigation.icons.ic_statistics
import org.jetbrains.compose.resources.StringResource

enum class NavigationDrawerRoute(
    val routeName: StringResource,
    val icon: ImageVector,
) {
    Sets(
        Res.string.navigation_route_sets,
        ic_sets,
    ),
    Cards(
        Res.string.navigation_route_cards,
        ic_cards,
    ),
    CardDetails(
        Res.string.navigation_route_card_details,
        ic_card_details,
    ),
    SetPointExchange(
        Res.string.navigation_route_set_point_exchange,
        ic_set_point_exchange,
    ),
    Statistics(
        Res.string.navigation_route_statistics,
        ic_statistics,
    ),
    Settings(
        Res.string.navigation_route_settings,
        ic_settings,
    )
}
