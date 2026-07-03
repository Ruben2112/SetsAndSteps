package com.heveamobile.mapbystep.ui.destinations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.mapbystep.theme.spacing
import com.heveamobile.mapbystep.ui.common.Card
import com.heveamobile.mapbystep.ui.common.DestinationCard
import com.heveamobile.mapbystep.ui.common.KeyValueRow
import com.heveamobile.mapbystep.ui.common.MapDropDownMenu
import com.heveamobile.mapbystep.ui.common.MapStatisticsList
import mapbystep.shared.generated.resources.Res
import mapbystep.shared.generated.resources.destinations_current_level
import mapbystep.shared.generated.resources.destinations_total_visits
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DestinationsScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<DestinationsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DestinationsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun DestinationsContent(
    modifier: Modifier = Modifier,
    state: DestinationsState,
    onAction: (DestinationsAction) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        if (state.maps.size > 1) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MapDropDownMenu(
                    maps = state.maps,
                    selectedMap = state.selectedMap
                        ?: state.maps.first(),
                    onItemSelected = { map -> onAction(DestinationsAction.SelectMap(map)) },
                )
            }
        }

        if (state.selectedMap != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onAction(DestinationsAction.ToggleProgressDisplay)
                        },
                    title = if (state.maps.size > 1) null else state.selectedMap.name,
                    bottomContent = {
                        KeyValueRow(
                            key = stringResource(Res.string.destinations_total_visits),
                            value = state.selectedMap.destinations
                                .sumOf { it.totalVisits }
                                .toString(),
                        )
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    ) {
                        KeyValueRow(
                            modifier = Modifier.padding(end = MaterialTheme.spacing.large),
                            key = stringResource(Res.string.destinations_current_level),
                            value = state.selectedMap.currentLevel.toString(),
                        )
                        MapStatisticsList(
                            map = state.selectedMap,
                            isExpanded = state.isProgressExpanded,
                        )
                    }
                }
            }

            items(
                state.selectedMap.destinations,
                key = { it.id },
            ) { destination ->
                DestinationCard(
                    destination = destination,
                    isRevealed = destination.isDiscovered,
                    raritySpoiler = true,
                    onClick = {
                        onAction(DestinationsAction.OpenDestinationInfo(destination.id))
                    },
                )
            }
        }
    }
}