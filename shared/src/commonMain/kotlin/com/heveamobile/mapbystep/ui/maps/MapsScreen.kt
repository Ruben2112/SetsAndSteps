package com.heveamobile.mapbystep.ui.maps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.mapbystep.FormatMode
import com.heveamobile.mapbystep.formatAmount
import com.heveamobile.mapbystep.theme.spacing
import com.heveamobile.mapbystep.ui.common.Card
import com.heveamobile.mapbystep.ui.common.KeyValueRow
import com.heveamobile.mapbystep.ui.common.MapStatisticsList
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.maps_level
import mapbystep.shared.generated.resources.maps_steps_per_visit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapsScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<MapsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    MapsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun MapsContent(
    modifier: Modifier = Modifier,
    state: MapsState,
    onAction: (MapsAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium),
    ) {
        if (state.maps.isNotEmpty()) {
            state.maps.forEach { map ->
                Card(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onAction(
                                MapsAction.ExpandProgress(map),
                            )
                        },
                    title = map.name,
                    subtitle = stringResource(
                        Res.string.maps_level,
                        map.currentLevel,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    ) {
                        KeyValueRow(
                            modifier = Modifier.padding(end = MaterialTheme.spacing.large),
                            key = stringResource(Res.string.maps_steps_per_visit),
                            value = formatAmount(
                                map.calculatedDistance,
                                FormatMode.Long,
                            ),
                        )
                        MapStatisticsList(
                            map = map,
                            isExpanded = state.expandedMapId == map.id,
                        )
                    }
                }
            }
        }
    }
}