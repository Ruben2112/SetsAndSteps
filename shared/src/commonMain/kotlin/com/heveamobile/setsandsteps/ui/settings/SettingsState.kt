package com.heveamobile.setsandsteps.ui.settings

import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import kotlinx.datetime.LocalTime

data class SettingsState(
    val isLoading: Boolean = false,

    val distanceMultiplier: Double = 1.0,
    val reminderIsEnabled: Boolean = false,
    val reminderTime: LocalTime = LocalTime(
        hour = 21,
        minute = 0,
    ),
    val showTimePickerAlertDialog: Boolean = false,
    val showImportConfirmationAlert: Boolean = false,
    val showNotificationSettingsDialog: Boolean = false,
    val hasRequestedNotificationPermission: Boolean = false,
    val notificationPermissionStatus: PermissionStatus = PermissionStatus.Loading,
)

sealed interface SettingsAction {
    data class UpdateDistanceMultiplier(val distanceMultiplier: Float) : SettingsAction
    data object ExportProgress : SettingsAction
    data object ImportProgress : SettingsAction
    data object ConfirmImport : SettingsAction
    data object CancelImport : SettingsAction
    data class UpdateReminderIsEnabled(val isEnabled: Boolean) : SettingsAction
    data object ToggleTimePickerAlertDialog : SettingsAction
    data class UpdateReminderTime(val time: LocalTime) : SettingsAction
    data object UpdateNotificationPermissionStatus : SettingsAction
    data object OpenAppSettings : SettingsAction
    data object ShowNotificationSettingsDialog : SettingsAction
    data object DismissNotificationSettingsDialog : SettingsAction
    data class UpdateHasRequestedNotificationPermission(val hasRequested: Boolean) : SettingsAction
}

sealed interface SettingsEvent {
    data object ExportSuccessful : SettingsEvent
    data object ExportFailed : SettingsEvent
    data object ImportSuccessful : SettingsEvent
    data object ImportFailed : SettingsEvent
}