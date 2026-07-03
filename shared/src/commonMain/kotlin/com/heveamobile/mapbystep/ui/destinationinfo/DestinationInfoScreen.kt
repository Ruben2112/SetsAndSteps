package com.heveamobile.mapbystep.ui.destinationinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.mapbystep.data.entity.InfoType
import com.heveamobile.mapbystep.navigation.Route
import com.heveamobile.mapbystep.theme.spacing
import com.heveamobile.mapbystep.ui.common.CountryInfoCard
import com.heveamobile.mapbystep.ui.common.DropDownMenu
import com.heveamobile.mapbystep.ui.common.MapDropDownMenu

@Composable
fun DestinationInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: DestinationInfoViewModel,
    route: Route.DestinationInfo,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(route.destinationId) {
        viewModel.loadDestinationInfo(route.destinationId)
    }

    DestinationInfoContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun DestinationInfoContent(
    modifier: Modifier = Modifier,
    state: DestinationInfoState,
    onAction: (DestinationInfoAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        if (state.maps.size > 1) {
            item {
                MapDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    maps = state.maps,
                    selectedMap = state.selectedMap
                        ?: state.maps.first(),
                    onItemSelected = { map -> onAction(DestinationInfoAction.SelectMap(map)) },
                )
            }
        }

        if (state.destinations.isNotEmpty()) {
            item {
                DropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    items = state.destinations,
                    selectedItem = state.selectedDestination
                        ?: state.selectedMap?.destinations?.first()
                        ?: state.maps.first().destinations.first(),
                    onItemSelected = { destination ->
                        onAction(DestinationInfoAction.SelectDestination(destination))
                    },
                ) { destination ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = destination.name,
                        style = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                }
            }
        }

        if (state.selectedMap != null && state.selectedDestination != null && state.info != null) {
            item {
                when (state.selectedMap.infoType) {
                    InfoType.Country -> CountryInfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        destination = state.selectedDestination,
                    )
                }
            }
        }
    }
}

