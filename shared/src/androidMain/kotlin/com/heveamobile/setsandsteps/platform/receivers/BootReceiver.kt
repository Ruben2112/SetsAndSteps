package com.heveamobile.setsandsteps.platform.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.heveamobile.setsandsteps.domain.manager.DailyReminderManager
import com.heveamobile.setsandsteps.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    // Use Koin's inject delegate for non-Compose classes
    private val userPreferencesRepository: UserPreferencesRepository by inject()
    private val dailyReminderManager: DailyReminderManager by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onReceive(
        context: Context,
        intent: Intent?,
    ) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()

            scope.launch {
                try {
                    val isEnabled = userPreferencesRepository.isReminderEnabled.first()
                    if (isEnabled) {
                        val time = userPreferencesRepository.reminderTime.first()
                        dailyReminderManager.scheduleDailyReminderNotification(time)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}