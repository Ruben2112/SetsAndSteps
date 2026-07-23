package com.heveamobile.setsandsteps.platform.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context

class NotificationManager {
    companion object {
        const val DAILY_REMINDER_NOTIFICATION_CHANNEL_ID = "daily_reminder_channel"

        fun createDailyReminderNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is not in the Support Library.
            val name = "Daily reminder notification"
            val descriptionText =
                "Used to get daily reminders to check the destinations you've visited during the day"
            val importance = IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                DAILY_REMINDER_NOTIFICATION_CHANNEL_ID,
                name,
                importance,
            ).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}