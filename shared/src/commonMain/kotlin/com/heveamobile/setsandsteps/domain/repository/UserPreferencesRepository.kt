package com.heveamobile.setsandsteps.domain.repository

import com.heveamobile.setsandsteps.ui.home.SortingOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime

interface UserPreferencesRepository {
    val gridSortingOrder: Flow<SortingOrder>
    val hideUndiscovered: Flow<Boolean>
    val isReminderEnabled: Flow<Boolean>
    val reminderTime: Flow<LocalTime>
    val distanceMultiplier: Flow<Double>
    val hasRequestedNotificationPermission: Flow<Boolean>
    val hasRequestedHealthPermission: Flow<Boolean>

    suspend fun updateGridSortingOrder(sortingOrder: SortingOrder)
    suspend fun updateHideUndiscovered(hideUndiscovered: Boolean)
    suspend fun updateIsReminderEnabled(isEnabled: Boolean)
    suspend fun updateReminderTime(time: LocalTime)
    suspend fun updateDistanceMultiplier(distanceMultiplier: Double)
    suspend fun updateHasRequestedNotificationPermission(hasRequested: Boolean)
    suspend fun updateHasRequestedHealthPermission(hasRequested: Boolean)
}
