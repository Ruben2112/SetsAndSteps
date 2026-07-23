package com.heveamobile.setsandsteps.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.usecase.GetDailyStepsChartDataUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetUserUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.SyncStepsUseCase
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    val permissionManager: PermissionManager,
    private val syncStepsUseCase: SyncStepsUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getDailyStepsChartDataUseCase: GetDailyStepsChartDataUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val permissionState = permissionManager.checkPermissionStatus(PermissionType.Health)
            _state.update { it.copy(healthPermissionState = permissionState) }

            combine(
                getUserUseCase(),
                getDailyStepsChartDataUseCase(),
                userPreferencesRepository.hasRequestedHealthPermission,
            ) { user, dailyStepData, hasRequestedHealthPermission ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        totalSteps = user?.totalSteps
                            ?: 0,
                        startTime = user?.startTime
                            ?: Clock.System.now(),
                        previousTwentyFourHours = user?.previousTwentyFourHours
                            ?: 0,
                        twentyFourHourRecord = user?.twentyFourHourRecord
                            ?: 0,
                        previousSevenDays = user?.previousSevenDays
                            ?: 0,
                        sevenDayRecord = user?.sevenDayRecord
                            ?: 0,
                        previousThirtyDays = user?.previousThirtyDays
                            ?: 0,
                        thirtyDayRecord = user?.thirtyDayRecord
                            ?: 0,
                        dailyStepData = dailyStepData,
                        hasRequestedHealthPermission = hasRequestedHealthPermission,
                    )
                }
            }.collectLatest { }
        }
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.UpdatePermissionState -> {
                viewModelScope.launch {
                    val permissionState =
                        permissionManager.checkPermissionStatus(PermissionType.Health)
                    _state.update { it.copy(healthPermissionState = permissionState) }

                    // Also sync steps if user has only just granted permissions
                    if (permissionState == PermissionStatus.Granted) {
                        syncStepsUseCase()
                    }
                }
            }

            is ProfileAction.UpdateHasRequestedHealthPermission -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateHasRequestedHealthPermission(action.hasRequested)
                }
            }

            ProfileAction.ShowHealthSettingsDialog -> {
                _state.update { it.copy(showHealthSettingsDialog = true) }
            }

            ProfileAction.DismissHealthSettingsDialog -> {
                _state.update { it.copy(showHealthSettingsDialog = false) }
            }

            ProfileAction.OpenAppSettings -> {
                permissionManager.openAppSettings()
                _state.update { it.copy(showHealthSettingsDialog = false) }
            }
        }
    }
}
