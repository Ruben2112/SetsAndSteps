package com.heveamobile.setsandsteps.platform.receivers

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.heveamobile.setsandsteps.shared.R
import com.heveamobile.setsandsteps.platform.manager.AndroidDailyReminderManager.Companion.DAILY_REMINDER_NOTIFICATION_REQUEST_CODE
import com.heveamobile.setsandsteps.platform.manager.NotificationManager

class DailyReminderReceiver : BroadcastReceiver() {
    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(
                context,
                DailyReminderReceiver::class.java,
            )
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val textTitle = context.getString(R.string.daily_reminder_notification_title)
        val textContent = context.getString(R.string.daily_reminder_notification_description)

        // Create an Intent for the activity you want to start.
        val resultIntent = Intent().setClassName(
            context.packageName,
            "com.heveamobile.setsandsteps.MainActivity",
        )
        val resultPendingIntent: PendingIntent? = TaskStackBuilder
            .create(context)
            .run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

        val notification = NotificationCompat
            .Builder(
                context,
                NotificationManager.DAILY_REMINDER_NOTIFICATION_CHANNEL_ID,
            )
            .setSmallIcon(R.drawable.ic_daily_reminder_notification)
            .setContentTitle(textTitle)
            .setAutoCancel(true)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                DAILY_REMINDER_NOTIFICATION_REQUEST_CODE,
                notification,
            )
    }
}