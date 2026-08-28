package com.heveamobile.setsandsteps.feature.statistics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.core.designsystem.component.AlertDialog
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.ErrorCard
import com.heveamobile.setsandsteps.core.designsystem.component.KeyValueRow
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.error_action_request_permissions
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.label_cancel
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.label_continue
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.permissions_not_granted_error
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.formatAmount
import com.heveamobile.setsandsteps.core.domain.formatDate
import com.heveamobile.setsandsteps.core.domain.formatDateTime
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType
import com.heveamobile.setsandsteps.core.presentation.rememberPermissionLauncher
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.Res
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.historic_step_data_start_time
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.historic_step_data_title
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.historic_step_data_total_steps
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.personal_current_vs_best_steps
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.personal_records_seven_days
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.personal_records_subtitle
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.personal_records_thirty_days
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.personal_records_title
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.personal_records_twenty_four_hours
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.statistics_error_health_connect_not_installed
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.statistics_health_permission_request_rationale
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.statistics_health_permission_request_title
import com.heveamobile.setsandsteps.feature.statistics.presentation.generated.resources.statistics_loading_step_data
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer.ColumnProvider
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Position
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<StatisticsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(StatisticsAction.UpdatePermissionState)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    val launcher = rememberPermissionLauncher(
        manager = viewModel.permissionManager,
        type = PermissionType.Health,
        onResult = { _ ->
            viewModel.onAction(StatisticsAction.UpdatePermissionState)
            viewModel.onAction(StatisticsAction.UpdateHasRequestedHealthPermission(true))
        },
    )

    StatisticsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onPermissionRequest = {
            if (state.healthPermissionState == PermissionStatus.NotGranted && state.hasRequestedHealthPermission) {
                viewModel.onAction(StatisticsAction.ShowHealthSettingsDialog)
            } else {
                launcher()
            }
        },
    )
}

@Composable
fun StatisticsContent(
    modifier: Modifier = Modifier,
    state: StatisticsState,
    onAction: (StatisticsAction) -> Unit,
    onPermissionRequest: () -> Unit,
) {
    if (state.showHealthSettingsDialog) {
        HealthSettingsDialog(onAction = onAction)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        if (state.isLoading) {
            item {
                Column(modifier = Modifier.animateItem()) {
                    Card {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                strokeWidth = 2.dp,
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            Text(
                                text = stringResource(Res.string.statistics_loading_step_data),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
        if (state.healthPermissionState == PermissionStatus.NotGranted || state.healthPermissionState == PermissionStatus.RationaleRequired) {
            item {
                ErrorCard(
                    errorMessage = stringResource(DesignSystemRes.string.permissions_not_granted_error),
                    modifier = Modifier.animateItem(),
                    actionLabel = stringResource(DesignSystemRes.string.error_action_request_permissions),
                    onAction = onPermissionRequest,
                )
            }
        } else if (state.healthPermissionState == PermissionStatus.NotInstalled) {
            item {
                ErrorCard(
                    errorMessage = stringResource(Res.string.statistics_error_health_connect_not_installed),
                    modifier = Modifier.animateItem(),
                )
            }
        }
        item {
            HistoricDataCard(state = state)
        }
        item {
            PersonalRecordsDataCard(state = state)
        }
    }
}

@Composable
private fun HealthSettingsDialog(onAction: (StatisticsAction) -> Unit) {
    AlertDialog(
        title = stringResource(Res.string.statistics_health_permission_request_title),
        body = stringResource(Res.string.statistics_health_permission_request_rationale),
        primaryActionLabel = stringResource(DesignSystemRes.string.label_continue),
        primaryAction = {
            onAction(StatisticsAction.OpenAppSettings)
        },
        secondaryActionLabel = stringResource(DesignSystemRes.string.label_cancel),
        secondaryAction = {
            onAction(StatisticsAction.DismissHealthSettingsDialog)
        },
        onDismissRequest = {
            onAction(StatisticsAction.DismissHealthSettingsDialog)
        },
    )
}

@Composable
private fun HistoricDataCard(state: StatisticsState) {
    Card(
        title = stringResource(Res.string.historic_step_data_title),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.height(160.dp)) {
                if (state.dailyStepData.isNotEmpty()) {
                    DailyStepsChart(state.dailyStepData)
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.historic_step_data_start_time),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = formatDateTime(
                    state.startTime,
                    FormatMode.Medium,
                ),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.historic_step_data_total_steps),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = formatAmount(
                    state.totalSteps,
                    FormatMode.Long,
                ),
            )
        }
    }
}

@Composable
private fun DailyStepsChart(dailyStepData: Map<Instant, Long>) {
    val modelProducer = remember { CartesianChartModelProducer() }

    val horizontalAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
        val instant = Instant.fromEpochSeconds(x.toLong())
        formatDate(
            instant,
            FormatMode.Short,
        )
    }

    val verticalAxisValueFormatter = CartesianValueFormatter { _, y, _ ->
        formatAmount(
            y.toLong(),
            FormatMode.Long,
        )
    }

    val dataValueFormatter = CartesianValueFormatter { _, value, _ ->
        formatAmount(
            value.toLong(),
            FormatMode.Long,
        )
    }

    LaunchedEffect(dailyStepData) {
        modelProducer.runTransaction {
            columnModel {
                series(
                    x = dailyStepData.keys.map { it.epochSeconds },
                    y = dailyStepData.values,
                )
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnProvider.series(
                    rememberLineComponent(
                        fill = Fill(MaterialTheme.colorScheme.primaryContainer),
                        thickness = 32.dp,
                        shape = MaterialTheme.shapes.small.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp),
                        ),
                    ),
                ),
                dataLabel = rememberTextComponent(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)),
                dataLabelPosition = Position.Vertical.Top,
                dataLabelValueFormatter = dataValueFormatter,
                columnCollectionSpacing = MaterialTheme.spacing.small,
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = verticalAxisValueFormatter,
                line = rememberAxisLineComponent(
                    fill = Fill(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                ),
                tick = rememberAxisTickComponent(
                    fill = Fill(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                ),
                guideline = rememberAxisGuidelineComponent(
                    fill = Fill(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                ),

                label = TextComponent(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)),
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = horizontalAxisValueFormatter,
                line = rememberAxisLineComponent(
                    fill = Fill(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                ),
                tick = rememberAxisTickComponent(
                    fill = Fill(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                ),
                guideline = null,
                label = TextComponent(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)),
            ),
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
    )
}

@Composable
private fun PersonalRecordsDataCard(state: StatisticsState) {
    Card(
        title = stringResource(Res.string.personal_records_title),
        subtitle = stringResource(Res.string.personal_records_subtitle),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.personal_records_twenty_four_hours),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = stringResource(
                    Res.string.personal_current_vs_best_steps,
                    formatAmount(
                        state.previousTwentyFourHours,
                        FormatMode.Long,
                    ),
                    formatAmount(
                        state.twentyFourHourRecord,
                        FormatMode.Long,
                    ),
                ),
                valueTitleCasingEnabled = false,
            )
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.personal_records_seven_days),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = stringResource(
                    Res.string.personal_current_vs_best_steps,
                    formatAmount(
                        state.previousSevenDays,
                        FormatMode.Long,
                    ),
                    formatAmount(
                        state.sevenDayRecord,
                        FormatMode.Long,
                    ),
                ),
                valueTitleCasingEnabled = false,
            )
            KeyValueRow(
                key = {
                    Text(
                        text = stringResource(Res.string.personal_records_thirty_days),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                value = stringResource(
                    Res.string.personal_current_vs_best_steps,
                    formatAmount(
                        state.previousThirtyDays,
                        FormatMode.Long,
                    ),
                    formatAmount(
                        state.thirtyDayRecord,
                        FormatMode.Long,
                    ),
                ),
                valueTitleCasingEnabled = false,
            )
        }
    }
}
