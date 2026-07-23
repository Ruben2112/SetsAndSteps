package com.heveamobile.setsandsteps

import android.app.Application
import com.heveamobile.setsandsteps.di.initializeKoin
import com.heveamobile.setsandsteps.core.data.manager.NotificationManager
import org.koin.android.ext.koin.androidContext

class SetsAndSteps : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {
                androidContext(this@SetsAndSteps)
            },
        )
        NotificationManager.createDailyReminderNotificationChannel(this)
    }
}