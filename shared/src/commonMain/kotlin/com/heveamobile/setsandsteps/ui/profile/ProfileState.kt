package com.heveamobile.setsandsteps.ui.profile

import com.heveamobile.setsandsteps.ui.common.PermissionStatus
import kotlin.time.Clock
import kotlin.time.Instant

data class ProfileState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val totalSteps: Long = 0L,
    val startTime: Instant = Clock.System.now(),
    val previousTwentyFourHours: Long = 0L,
    val twentyFourHourRecord: Long = 0L,
    val previousSevenDays: Long = 0L,
    val sevenDayRecord: Long = 0L,
    val previousThirtyDays: Long = 0L,
    val thirtyDayRecord: Long = 0L,
    val dailyStepData: Map<Instant, Long> = emptyMap(),

    val healthPermissionState: PermissionStatus = PermissionStatus.Loading,
    val hasRequestedHealthPermission: Boolean = false,
    val showHealthSettingsDialog: Boolean = false,
)

sealed interface ProfileAction {
    data object UpdatePermissionState : ProfileAction
    data class UpdateHasRequestedHealthPermission(val hasRequested: Boolean) : ProfileAction
    data object ShowHealthSettingsDialog : ProfileAction
    data object DismissHealthSettingsDialog : ProfileAction
    data object OpenAppSettings : ProfileAction
}
