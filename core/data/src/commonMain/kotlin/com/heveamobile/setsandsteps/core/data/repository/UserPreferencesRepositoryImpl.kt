package com.heveamobile.setsandsteps.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.model.SortingOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalTime

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {
    private object Keys {
        val GRID_SORTING_ORDER = stringPreferencesKey("grid_sorting_order")
        val HIDE_UNDISCOVERED = booleanPreferencesKey("hide_undiscovered")
        val IS_REMINDER_ENABLED = booleanPreferencesKey("is_reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
        val DISTANCE_MULTIPLIER = doublePreferencesKey("distance_multiplier")
        val HAS_REQUESTED_NOTIFICATION_PERMISSION =
            booleanPreferencesKey("has_requested_notification_permission")
        val HAS_REQUESTED_HEALTH_PERMISSION =
            booleanPreferencesKey("has_requested_health_permission")
    }

    override val gridSortingOrder: Flow<SortingOrder> = dataStore.data.map { prefs ->
        SortingOrder.valueOf(
            prefs[Keys.GRID_SORTING_ORDER]
                ?: SortingOrder.Rarity.name,
        )
    }

    override val hideUndiscovered: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.HIDE_UNDISCOVERED]
            ?: false
    }

    override val isReminderEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.IS_REMINDER_ENABLED]
            ?: false
    }
    override val reminderTime: Flow<LocalTime> = dataStore.data.map { prefs ->
        val reminderTime = prefs[Keys.REMINDER_TIME]
        if (reminderTime == null) {
            LocalTime(
                hour = 21,
                minute = 0,
            )
        } else {
            LocalTime.parse(reminderTime)
        }
    }

    override val distanceMultiplier: Flow<Double> = dataStore.data.map { prefs ->
        prefs[Keys.DISTANCE_MULTIPLIER]
            ?: 1.0
    }

    override val hasRequestedNotificationPermission: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.HAS_REQUESTED_NOTIFICATION_PERMISSION]
            ?: false
    }

    override val hasRequestedHealthPermission: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.HAS_REQUESTED_HEALTH_PERMISSION]
            ?: false
    }

    override suspend fun updateGridSortingOrder(sortingOrder: SortingOrder) {
        dataStore.edit { it[Keys.GRID_SORTING_ORDER] = sortingOrder.name }
    }

    override suspend fun updateHideUndiscovered(hideUndiscovered: Boolean) {
        dataStore.edit { it[Keys.HIDE_UNDISCOVERED] = hideUndiscovered }
    }

    override suspend fun updateIsReminderEnabled(isEnabled: Boolean) {
        dataStore.edit { it[Keys.IS_REMINDER_ENABLED] = isEnabled }
    }

    override suspend fun updateReminderTime(time: LocalTime) {
        dataStore.edit { it[Keys.REMINDER_TIME] = time.toString() }
    }

    override suspend fun updateDistanceMultiplier(distanceMultiplier: Double) {
        dataStore.edit { it[Keys.DISTANCE_MULTIPLIER] = distanceMultiplier }
    }

    override suspend fun updateHasRequestedNotificationPermission(hasRequested: Boolean) {
        dataStore.edit { it[Keys.HAS_REQUESTED_NOTIFICATION_PERMISSION] = hasRequested }
    }

    override suspend fun updateHasRequestedHealthPermission(hasRequested: Boolean) {
        dataStore.edit { it[Keys.HAS_REQUESTED_HEALTH_PERMISSION] = hasRequested }
    }
}
