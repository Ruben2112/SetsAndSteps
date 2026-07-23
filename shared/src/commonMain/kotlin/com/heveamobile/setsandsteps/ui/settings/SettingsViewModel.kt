package com.heveamobile.setsandsteps.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.manager.DailyReminderManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType
import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.usecase.ExportDatabaseUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.ImportDatabaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.round

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    val filePickerHandler: FilePickerHandler,
    private val exportDatabaseUseCase: ExportDatabaseUseCase,
    private val importDatabaseUseCase: ImportDatabaseUseCase,
    private val dailyReminderManager: DailyReminderManager,
    val permissionManager: PermissionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val notificationPermissionStatus =
                permissionManager.checkPermissionStatus(PermissionType.Notifications)
            _state.update { it.copy(notificationPermissionStatus = notificationPermissionStatus) }

            combine(
                userPreferencesRepository.distanceMultiplier,
                userPreferencesRepository.isReminderEnabled,
                userPreferencesRepository.reminderTime,
                userPreferencesRepository.hasRequestedNotificationPermission,
            ) { distanceMultiplier, reminderIsEnabled, reminderTime, hasRequestedNotificationPermission ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        distanceMultiplier = distanceMultiplier,
                        reminderIsEnabled = reminderIsEnabled,
                        reminderTime = reminderTime,
                        hasRequestedNotificationPermission = hasRequestedNotificationPermission,
                    )
                }
            }.collect()
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.UpdateDistanceMultiplier -> {
                viewModelScope.launch {
                    // Round to 1 decimal place to fix floating point precision errors
                    val roundedValue = round(action.distanceMultiplier * 10F) / 10.0
                    _state.update { it.copy(distanceMultiplier = roundedValue) }
                    userPreferencesRepository.updateDistanceMultiplier(roundedValue)
                }
            }

            is SettingsAction.ExportProgress -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = exportDatabaseUseCase()
                    if (result.isSuccess) {
                        _events.send(SettingsEvent.ExportSuccessful)
                    } else {
                        _events.send(SettingsEvent.ExportFailed)
                    }
                }
            }

            is SettingsAction.ImportProgress -> {
                viewModelScope.launch {
                    _state.update { it.copy(showImportConfirmationAlert = true) }
                }
            }

            is SettingsAction.CancelImport -> {
                viewModelScope.launch {
                    _state.update { it.copy(showImportConfirmationAlert = false) }
                }
            }

            is SettingsAction.ConfirmImport -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(showImportConfirmationAlert = false) }
                val result = importDatabaseUseCase()
                if (result.isSuccess) {
                    _events.send(SettingsEvent.ImportSuccessful)
                } else {
                    _events.send(SettingsEvent.ImportFailed)
                }
            }

            is SettingsAction.UpdateReminderIsEnabled -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateIsReminderEnabled(action.isEnabled)
                    if (action.isEnabled) {
                        dailyReminderManager.scheduleDailyReminderNotification(state.value.reminderTime)
                    } else {
                        dailyReminderManager.cancelDailyReminderNotification()
                    }
                }
            }

            is SettingsAction.UpdateReminderTime -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateReminderTime(action.time)
                    if (state.value.reminderIsEnabled) {
                        dailyReminderManager.scheduleDailyReminderNotification(action.time)
                    }
                }
            }

            is SettingsAction.ToggleTimePickerAlertDialog -> {
                viewModelScope.launch {
                    _state.update { it.copy(showTimePickerAlertDialog = !it.showTimePickerAlertDialog) }
                }
            }

            is SettingsAction.UpdateNotificationPermissionStatus -> {
                viewModelScope.launch {
                    val status =
                        permissionManager.checkPermissionStatus(PermissionType.Notifications)
                    _state.update { it.copy(notificationPermissionStatus = status) }
                }
            }

            is SettingsAction.OpenAppSettings -> {
                permissionManager.openAppSettings()
                _state.update { it.copy(showNotificationSettingsDialog = false) }
            }

            is SettingsAction.ShowNotificationSettingsDialog -> {
                _state.update { it.copy(showNotificationSettingsDialog = true) }
            }

            is SettingsAction.DismissNotificationSettingsDialog -> {
                _state.update { it.copy(showNotificationSettingsDialog = false) }
            }

            is SettingsAction.UpdateHasRequestedNotificationPermission -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateHasRequestedNotificationPermission(action.hasRequested)
                }
            }
        }
    }
}