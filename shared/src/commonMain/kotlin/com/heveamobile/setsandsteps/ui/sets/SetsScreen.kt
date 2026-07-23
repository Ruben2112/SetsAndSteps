package com.heveamobile.setsandsteps.ui.sets

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
import com.heveamobile.setsandsteps.FormatMode
import com.heveamobile.setsandsteps.formatAmount
import com.heveamobile.setsandsteps.theme.spacing
import com.heveamobile.setsandsteps.ui.common.Card
import com.heveamobile.setsandsteps.ui.common.KeyValueRow
import com.heveamobile.setsandsteps.ui.common.SetStatisticsList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.sets_level
import setsandsteps.shared.generated.resources.sets_steps_per_finding

@Composable
fun SetsScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<SetsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SetsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SetsContent(
    modifier: Modifier = Modifier,
    state: SetsState,
    onAction: (SetsAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium),
    ) {
        if (state.sets.isNotEmpty()) {
            state.sets.forEach { map ->
                if (map.userData == null) return

                Card(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onAction(
                                SetsAction.ExpandProgress(map),
                            )
                        },
                    title = map.name,
                    subtitle = stringResource(
                        Res.string.sets_level,
                        map.userData.currentLevel,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    ) {
                        KeyValueRow(
                            modifier = Modifier.padding(end = MaterialTheme.spacing.large),
                            key = stringResource(Res.string.sets_steps_per_finding),
                            value = formatAmount(
                                map.userData.calculatedDistance,
                                FormatMode.Long,
                            ),
                        )
                        SetStatisticsList(
                            set = map,
                            isExpanded = state.expandedSetId == map.id,
                        )
                    }
                }
            }
        }
    }
}