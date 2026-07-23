package com.heveamobile.setsandsteps.platform.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.heveamobile.setsandsteps.core.domain.manager.DailyReminderManager
import com.heveamobile.setsandsteps.platform.receivers.BootReceiver
import com.heveamobile.setsandsteps.platform.receivers.DailyReminderReceiver
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class AndroidDailyReminderManager(private val context: Context) : DailyReminderManager {

    companion object {
        const val DAILY_REMINDER_NOTIFICATION_REQUEST_CODE = 25062026
    }

    override fun scheduleDailyReminderNotification(time: LocalTime) {
        cancelDailyReminderNotification()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val today = now.toLocalDateTime(timeZone).date

        var targetDateTime = LocalDateTime(
            today,
            time,
        )
        if (targetDateTime.toInstant(timeZone) <= now) {
            // If time already passed, move to tomorrow
            val tomorrow = today.plus(
                1,
                DateTimeUnit.DAY,
            )
            targetDateTime = LocalDateTime(
                tomorrow,
                time,
            )
        }

        val triggerAtMillis = targetDateTime
            .toInstant(timeZone)
            .toEpochMilliseconds()

        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            triggerAtMillis,
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                context,
                DAILY_REMINDER_NOTIFICATION_REQUEST_CODE,
                DailyReminderReceiver.getIntent(context),
                PendingIntent.FLAG_IMMUTABLE,
            ),
        )

        val receiver = ComponentName(
            context,
            BootReceiver::class.java,
        )
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP,
        )

        enableBootReceiver(enabled = true)
    }

    override fun cancelDailyReminderNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                DAILY_REMINDER_NOTIFICATION_REQUEST_CODE,
                DailyReminderReceiver.getIntent(context),
                PendingIntent.FLAG_IMMUTABLE,
            ),
        )

        enableBootReceiver(enabled = false)
    }

    /**
     * Enables or disables the [BootReceiver] which reschedules alarms after rebooting the device.
     * TODO: This works fine while we only have one alarm set (for daily reminders), but should
     * be reworked if we ever have multiple alarms
     */
    private fun enableBootReceiver(enabled: Boolean) {
        val receiver = ComponentName(
            context,
            BootReceiver::class.java,
        )

        context.packageManager.setComponentEnabledSetting(
            receiver,
            if (enabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP,
        )
    }
}