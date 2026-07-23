package com.heveamobile.setsandsteps.ui.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType
import com.heveamobile.setsandsteps.core.presentation.rememberPermissionLauncher
import com.heveamobile.setsandsteps.core.domain.formatAmount
import com.heveamobile.setsandsteps.core.domain.formatDate
import com.heveamobile.setsandsteps.core.domain.formatDateTime
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.designsystem.component.AlertDialog
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.ErrorCard
import com.heveamobile.setsandsteps.core.designsystem.component.KeyValueRow
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
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
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.historic_step_data_start_time
import setsandsteps.shared.generated.resources.historic_step_data_title
import setsandsteps.shared.generated.resources.historic_step_data_total_steps
import setsandsteps.shared.generated.resources.label_cancel
import setsandsteps.shared.generated.resources.label_continue
import setsandsteps.shared.generated.resources.permissions_not_granted_error
import setsandsteps.shared.generated.resources.personal_current_vs_best_steps
import setsandsteps.shared.generated.resources.personal_records_seven_days
import setsandsteps.shared.generated.resources.personal_records_subtitle
import setsandsteps.shared.generated.resources.personal_records_thirty_days
import setsandsteps.shared.generated.resources.personal_records_title
import setsandsteps.shared.generated.resources.personal_records_twenty_four_hours
import setsandsteps.shared.generated.resources.profile_error_action_request_permissions
import setsandsteps.shared.generated.resources.profile_error_health_connect_not_installed
import setsandsteps.shared.generated.resources.profile_health_permission_request_rationale
import setsandsteps.shared.generated.resources.profile_health_permission_request_title
import setsandsteps.shared.generated.resources.profile_loading_step_data
import kotlin.time.Instant

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(ProfileAction.UpdatePermissionState)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    val launcher = rememberPermissionLauncher(
        manager = viewModel.permissionManager,
        type = PermissionType.Health,
        onResult = { _ ->
            viewModel.onAction(ProfileAction.UpdatePermissionState)
            viewModel.onAction(ProfileAction.UpdateHasRequestedHealthPermission(true))
        },
    )

    ProfileContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onPermissionRequest = {
            if (state.healthPermissionState == PermissionStatus.NotGranted && state.hasRequestedHealthPermission) {
                viewModel.onAction(ProfileAction.ShowHealthSettingsDialog)
            } else {
                launcher()
            }
        },
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onPermissionRequest: () -> Unit,
) {
    AnimatedVisibility(visible = state.showHealthSettingsDialog) {
        HealthSettingsDialog(onAction = onAction)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.medium)
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        AnimatedVisibility(state.isLoading) {
            Column {
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
                            text = stringResource(Res.string.profile_loading_step_data),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
        AnimatedContent(targetState = state.healthPermissionState) { permissionState ->
            when (permissionState) {
                PermissionStatus.Loading -> {}
                PermissionStatus.Granted -> {}
                PermissionStatus.NotGranted, PermissionStatus.RationaleRequired -> Column(modifier = Modifier.fillMaxWidth()) {
                    ErrorCard(
                        errorMessage = stringResource(Res.string.permissions_not_granted_error),
                        actionLabel = stringResource(Res.string.profile_error_action_request_permissions),
                        onAction = onPermissionRequest,
                    )
                }

                PermissionStatus.NotInstalled -> Column(modifier = Modifier.fillMaxWidth()) {
                    ErrorCard(errorMessage = stringResource(Res.string.profile_error_health_connect_not_installed))
                }
            }
        }
        HistoricDataCard(state = state)
        PersonalRecordsDataCard(state = state)
    }
}

@Composable
private fun HealthSettingsDialog(onAction: (ProfileAction) -> Unit) {
    AlertDialog(
        title = stringResource(Res.string.profile_health_permission_request_title),
        body = stringResource(Res.string.profile_health_permission_request_rationale),
        primaryActionLabel = stringResource(Res.string.label_continue),
        primaryAction = {
            onAction(ProfileAction.OpenAppSettings)
        },
        secondaryActionLabel = stringResource(Res.string.label_cancel),
        secondaryAction = {
            onAction(ProfileAction.DismissHealthSettingsDialog)
        },
        onDismissRequest = {
            onAction(ProfileAction.DismissHealthSettingsDialog)
        },
    )
}

@Composable
private fun HistoricDataCard(state: ProfileState) {
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
                key = stringResource(Res.string.historic_step_data_start_time),
                value = formatDateTime(
                    state.startTime,
                    FormatMode.Medium,
                ),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            KeyValueRow(
                key = stringResource(Res.string.historic_step_data_total_steps),
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
                        fill = Fill(MaterialTheme.colorScheme.onBackground),
                        thickness = 32.dp,
                        shape = MaterialTheme.shapes.small.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp),
                        ),
                    ),
                ),
                dataLabel = rememberTextComponent(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)),
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
private fun PersonalRecordsDataCard(state: ProfileState) {
    Card(
        title = stringResource(Res.string.personal_records_title),
        subtitle = stringResource(Res.string.personal_records_subtitle),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            KeyValueRow(
                key = stringResource(Res.string.personal_records_twenty_four_hours),
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
                key = stringResource(Res.string.personal_records_seven_days),
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
                key = stringResource(Res.string.personal_records_thirty_days),
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
