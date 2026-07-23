package com.heveamobile.setsandsteps.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.heveamobile.setsandsteps.data.source.android.AndroidFilePickerHandler
import com.heveamobile.setsandsteps.data.source.android.AndroidFileRepositoryImpl
import com.heveamobile.setsandsteps.data.source.android.HealthConnectManager
import com.heveamobile.setsandsteps.data.source.local.AppDatabase
import com.heveamobile.setsandsteps.data.source.local.getDatabaseBuilder
import com.heveamobile.setsandsteps.data.source.remote.HealthDataSource
import com.heveamobile.setsandsteps.domain.manager.AndroidPermissionManager
import com.heveamobile.setsandsteps.domain.manager.DailyReminderManager
import com.heveamobile.setsandsteps.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.domain.repository.FilePickerHandler
import com.heveamobile.setsandsteps.domain.repository.FileRepository
import com.heveamobile.setsandsteps.platform.manager.AndroidDailyReminderManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.dsl.onClose

// DataStore must be a process-level singleton to avoid "multiple DataStores active" error.
// We cache it here so it survives Koin module reloads during database import.
private var dataStoreInstance: DataStore<Preferences>? = null

const val PREFS_FILE_NAME = "user_prefs.preferences_pb"

actual val targetModule = module {
    single { getDatabaseBuilder(get()).build() } onClose { it?.close() }

    single<DataStore<Preferences>> {
        // Reuse the existing DataStore instance if it exists to avoid IllegalStateException
        // and ensure the CoroutineScope remains active after module reloads.
        dataStoreInstance
            ?: PreferenceDataStoreFactory
                .create(
                    produceFile = { androidContext().filesDir.resolve(PREFS_FILE_NAME) },
                )
                .also { dataStoreInstance = it }
    }

    single { get<AppDatabase>().getUserDao() }
    single { get<AppDatabase>().getStepDataDao() }
    single { get<AppDatabase>().getCardSetDao() }
    single { get<AppDatabase>().getCardSetUserDataDao() }
    single { get<AppDatabase>().getCardDao() }
    single { get<AppDatabase>().getCardUserDataDao() }

    singleOf(::AndroidFilePickerHandler) { bind<FilePickerHandler>() }
    singleOf(::AndroidFileRepositoryImpl) { bind<FileRepository>() }

    singleOf(::AndroidPermissionManager) { bind<PermissionManager>() }
    singleOf(::AndroidDailyReminderManager) { bind<DailyReminderManager>() }
    singleOf(::HealthConnectManager) { bind<HealthDataSource>() }
}
