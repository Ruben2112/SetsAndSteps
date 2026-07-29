package com.heveamobile.setsandsteps.feature.statistics.presentation

import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import kotlin.time.Clock
import kotlin.time.Instant

data class StatisticsState(
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

sealed interface StatisticsAction {
    data object UpdatePermissionState : StatisticsAction
    data class UpdateHasRequestedHealthPermission(val hasRequested: Boolean) : StatisticsAction
    data object ShowHealthSettingsDialog : StatisticsAction
    data object DismissHealthSettingsDialog : StatisticsAction
    data object OpenAppSettings : StatisticsAction
}
