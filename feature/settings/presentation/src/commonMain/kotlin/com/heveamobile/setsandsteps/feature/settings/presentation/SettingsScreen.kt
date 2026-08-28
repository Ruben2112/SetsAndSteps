package com.heveamobile.setsandsteps.feature.settings.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.CenteredTrack
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberSliderState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.core.designsystem.component.AlertDialog
import com.heveamobile.setsandsteps.core.designsystem.component.Card
import com.heveamobile.setsandsteps.core.designsystem.component.ErrorCard
import com.heveamobile.setsandsteps.core.designsystem.component.PrimaryButton
import com.heveamobile.setsandsteps.core.designsystem.component.SecondaryButton
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.error_action_request_permissions
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.label_cancel
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.label_continue
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.label_ok
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.label_save
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.permissions_not_granted_error
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.FormatMode
import com.heveamobile.setsandsteps.core.domain.formatTime
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import com.heveamobile.setsandsteps.core.presentation.FilePickerHandlerEffect
import com.heveamobile.setsandsteps.core.presentation.LocalSnackbarHostState
import com.heveamobile.setsandsteps.core.presentation.rememberPermissionLauncher
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.Res
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_daily_reminder_change_time
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_daily_reminder_enable_daily_reminder
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_daily_reminder_explanation
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_daily_reminder_reminder_time
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_daily_reminder_title
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_distance_multiplier
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_distance_multiplier_explanation
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_distance_multiplier_title
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_error_notifications_not_granted
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_export
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_export_failed
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_export_import_explanation
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_export_import_title
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_export_successful
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_import
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_import_confirmation_dialog_body
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_import_failed
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_import_successful
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_notification_permission_request_rationale
import com.heveamobile.setsandsteps.feature.settings.presentation.generated.resources.settings_notification_permission_request_title
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import com.heveamobile.setsandsteps.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {

    val viewModel = koinViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = LocalSnackbarHostState.current

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(SettingsAction.UpdateNotificationPermissionStatus)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                SettingsEvent.ExportSuccessful -> snackbarHostState.showSnackbar(
                    getString(Res.string.settings_export_successful),
                )

                SettingsEvent.ExportFailed -> snackbarHostState.showSnackbar(
                    getString(Res.string.settings_export_failed),
                )

                SettingsEvent.ImportSuccessful -> snackbarHostState.showSnackbar(
                    getString(Res.string.settings_import_successful),
                )

                SettingsEvent.ImportFailed -> snackbarHostState.showSnackbar(
                    getString(Res.string.settings_import_failed),
                )

            }
        }
    }

    FilePickerHandlerEffect(viewModel.filePickerHandler)

    val launcher = rememberPermissionLauncher(
        manager = viewModel.permissionManager,
        type = com.heveamobile.setsandsteps.core.domain.manager.PermissionType.Notifications,
        onResult = { _ ->
            viewModel.onAction(SettingsAction.UpdateNotificationPermissionStatus)
            viewModel.onAction(SettingsAction.UpdateHasRequestedNotificationPermission(true))
        },
    )

    SettingsContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onPermissionRequest = {
            if (state.notificationPermissionStatus == PermissionStatus.NotGranted && state.hasRequestedNotificationPermission) {
                viewModel.onAction(SettingsAction.ShowNotificationSettingsDialog)
            } else {
                launcher()
            }
        },
    )
}

@OptIn(
    KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onPermissionRequest: () -> Unit,
) {

    if (state.showExplanationDialog) {
        AlertDialog(
            title = state.explanationDialogTitle
                ?: "",
            body = state.explanationDialogBody
                ?: "",
            onDismissRequest = { onAction(SettingsAction.HideExplanationDialog) },
            primaryActionLabel = stringResource(DesignSystemRes.string.label_ok),
            primaryAction = { onAction(SettingsAction.HideExplanationDialog) },
        )
    }

    if (state.showImportConfirmationAlert) {
        ImportConfirmationDialog(onAction = onAction)
    }

    if (state.showTimePickerAlertDialog) {
        TimePickerDialog(
            initialTime = state.reminderTime,
            onAction = onAction,
        )
    }

    if (state.showNotificationSettingsDialog) {
        NotificationSettingsDialog(onAction = onAction)
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        item {
            ReminderCard(
                reminderIsEnabled = state.reminderIsEnabled,
                reminderTime = state.reminderTime,
                notificationPermissionStatus = state.notificationPermissionStatus,
                onAction = onAction,
                onPermissionRequest = onPermissionRequest,
            )
        }
        item {
            DistanceMultiplierCard(
                distanceMultiplier = state.distanceMultiplier,
                onAction = onAction,
            )
        }
        item {
            ExportImportDataCard(onAction = onAction)
        }
    }
}

@Composable
private fun ReminderCard(
    reminderIsEnabled: Boolean,
    reminderTime: LocalTime,
    notificationPermissionStatus: PermissionStatus,
    onAction: (SettingsAction) -> Unit,
    onPermissionRequest: () -> Unit,
) {
    val title = stringResource(Res.string.settings_daily_reminder_title)
    val explanation = stringResource(Res.string.settings_daily_reminder_explanation)
    Card(
        title = title,
        onExplanationClick = {
            onAction(
                SettingsAction.ShowExplanationDialog(
                    title = title,
                    body = explanation,
                ),
            )
        },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.End,
        ) {
            AnimatedContent(
                targetState = notificationPermissionStatus,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(
                            220,
                            delayMillis = 90,
                        ),
                    ) togetherWith fadeOut(animationSpec = tween(90))
                },
                label = "NotificationPermissionStatus",
            ) { status ->
                when (status) {
                    PermissionStatus.Granted -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            horizontalAlignment = Alignment.End,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = stringResource(Res.string.settings_daily_reminder_enable_daily_reminder),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Switch(
                                    checked = reminderIsEnabled,
                                    onCheckedChange = { onAction(SettingsAction.UpdateReminderIsEnabled(it)) },
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = MaterialTheme.spacing.medium),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = stringResource(Res.string.settings_daily_reminder_reminder_time),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = formatTime(
                                        localTime = reminderTime,
                                        formatMode = FormatMode.Short,
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }

                            PrimaryButton(
                                label = stringResource(Res.string.settings_daily_reminder_change_time),
                            ) {
                                onAction(SettingsAction.ToggleTimePickerAlertDialog)
                            }
                        }
                    }

                    PermissionStatus.NotGranted, PermissionStatus.RationaleRequired -> {
                        ErrorCard(
                            errorMessage = stringResource(
                                if (status == PermissionStatus.RationaleRequired) {
                                    DesignSystemRes.string.permissions_not_granted_error
                                } else {
                                    Res.string.settings_error_notifications_not_granted
                                },
                            ),
                            actionLabel = stringResource(DesignSystemRes.string.error_action_request_permissions),
                            onAction = onPermissionRequest,
                        )
                    }

                    else -> {
                        // Other states are not interesting here
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: LocalTime,
    onAction: (SettingsAction) -> Unit,
) {
    val state = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
    )

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = { onAction(SettingsAction.ToggleTimePickerAlertDialog) },
        confirmButton = {
            PrimaryButton(label = stringResource(DesignSystemRes.string.label_save)) {
                onAction(
                    SettingsAction.UpdateReminderTime(
                        LocalTime(
                            state.hour,
                            state.minute,
                        ),
                    ),
                )
                onAction(SettingsAction.ToggleTimePickerAlertDialog)
            }
        },
        dismissButton = {
            SecondaryButton(label = stringResource(DesignSystemRes.string.label_cancel)) {
                onAction(SettingsAction.ToggleTimePickerAlertDialog)
            }
        },
        text = {
            TimePicker(state = state)
        },
    )
}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
private fun DistanceMultiplierCard(
    distanceMultiplier: Double,
    onAction: (SettingsAction) -> Unit,
) {
    val minValue = -1F
    val maxValue = 1F

    val sliderState = rememberSliderState(
        value = distanceMultiplier.toFloat() - 1F,
        valueRange = minValue..maxValue,
        // steps = all valid floats with a .1 decimal precision
        // (difference between maxValue times 10 and minValue times 10) (20)
        // plus the center point (0.0F) (1)
        // minus the slider's endpoints (-2)
        // which adds up to 19 steps
        steps = ((((maxValue.toInt() * 10) - (minValue.toInt() * 10)) + 1) - 2),
    )

    LaunchedEffect(distanceMultiplier) {
        sliderState.value = distanceMultiplier.toFloat() - 1F
    }

    sliderState.onValueChangeFinished = {
        onAction(SettingsAction.UpdateDistanceMultiplier(sliderState.value + 1F))
    }

    val title = stringResource(Res.string.settings_distance_multiplier_title)
    val explanation = stringResource(Res.string.settings_distance_multiplier_explanation)
    Card(
        title = title,
        onExplanationClick = {
            onAction(
                SettingsAction.ShowExplanationDialog(
                    title = title,
                    body = explanation,
                ),
            )
        },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            ) {
                Text(
                    text = stringResource(Res.string.settings_distance_multiplier),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    modifier = Modifier.weight(1F),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Slider(
                        modifier = Modifier.weight(1F),
                        state = sliderState,
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.onSurface),
                                contentAlignment = Alignment.Center,
                            ) {}
                        },
                        track = { state ->
                            CenteredTrack(
                                modifier = Modifier.height(8.dp),
                                colors = SliderDefaults
                                    .colors()
                                    .copy(
                                        activeTickColor = Color.Transparent,
                                        inactiveTickColor = Color.Transparent,
                                    ),
                                drawStopIndicator = {},
                                thumbTrackGapSize = 0.dp,
                                sliderState = state,
                            )
                        },
                    )
                    Text(
                        modifier = Modifier.width(40.dp),
                        text = "${distanceMultiplier}x",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun ExportImportDataCard(
    onAction: (SettingsAction) -> Unit,
) {
    val title = stringResource(Res.string.settings_export_import_title)
    val explanation = stringResource(Res.string.settings_export_import_explanation)
    Card(
        title = title,
        onExplanationClick = {
            onAction(
                SettingsAction.ShowExplanationDialog(
                    title = title,
                    body = explanation,
                ),
            )
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.medium,
                Alignment.End,
            ),
        ) {
            PrimaryButton(label = stringResource(Res.string.settings_export)) {
                onAction(SettingsAction.ExportProgress)
            }
            PrimaryButton(label = stringResource(Res.string.settings_import)) {
                onAction(SettingsAction.ImportProgress)
            }
        }
    }
}

@Composable
private fun ImportConfirmationDialog(onAction: (SettingsAction) -> Unit) {
    AlertDialog(
        title = stringResource(Res.string.settings_import),
        body = stringResource(Res.string.settings_import_confirmation_dialog_body),
        primaryActionLabel = stringResource(Res.string.settings_import),
        primaryAction = {
            onAction(SettingsAction.ConfirmImport)
        },
        isPrimaryActionDestructive = true,
        secondaryActionLabel = stringResource(DesignSystemRes.string.label_cancel),
        secondaryAction = {
            onAction(SettingsAction.CancelImport)
        },
        onDismissRequest = {
            onAction(SettingsAction.CancelImport)
        },
    )
}

@Composable
private fun NotificationSettingsDialog(onAction: (SettingsAction) -> Unit) {
    AlertDialog(
        title = stringResource(Res.string.settings_notification_permission_request_title),
        body = stringResource(Res.string.settings_notification_permission_request_rationale),
        primaryActionLabel = stringResource(DesignSystemRes.string.label_continue),
        primaryAction = {
            onAction(SettingsAction.OpenAppSettings)
        },
        secondaryActionLabel = stringResource(DesignSystemRes.string.label_cancel),
        secondaryAction = {
            onAction(SettingsAction.DismissNotificationSettingsDialog)
        },
        onDismissRequest = {
            onAction(SettingsAction.DismissNotificationSettingsDialog)
        },
    )
}