package com.heveamobile.setsandsteps

import android.app.Application
import com.heveamobile.setsandsteps.core.data.manager.NotificationManager
import com.heveamobile.setsandsteps.core.domain.repository.CardSetCatalogRepository
import com.heveamobile.setsandsteps.di.initializeKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class SetsAndSteps : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {
                androidContext(this@SetsAndSteps)
            },
        )
        NotificationManager.createDailyReminderNotificationChannel(this)

        val koin = GlobalContext.get()
        koin
            .get<CoroutineScope>()
            .launch {
                koin
                    .get<CardSetCatalogRepository>()
                    .refreshCatalog()
            }
    }
}