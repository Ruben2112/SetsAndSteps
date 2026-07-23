package com.heveamobile.setsandsteps.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.heveamobile.setsandsteps.core.data.manager.AndroidDailyReminderManager
import com.heveamobile.setsandsteps.core.data.manager.AndroidPermissionManager
import com.heveamobile.setsandsteps.core.data.repository.CardSetRepositoryImpl
import com.heveamobile.setsandsteps.core.data.repository.CollectableCardRepositoryImpl
import com.heveamobile.setsandsteps.core.data.repository.StepDataRepositoryImpl
import com.heveamobile.setsandsteps.core.data.repository.UserPreferencesRepositoryImpl
import com.heveamobile.setsandsteps.core.data.repository.UserRepositoryImpl
import com.heveamobile.setsandsteps.core.data.source.android.AndroidFilePickerHandler
import com.heveamobile.setsandsteps.core.data.source.android.AndroidFileRepositoryImpl
import com.heveamobile.setsandsteps.core.data.source.android.HealthConnectManager
import com.heveamobile.setsandsteps.core.domain.manager.DailyReminderManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository
import com.heveamobile.setsandsteps.core.domain.repository.CollectableCardRepository
import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler
import com.heveamobile.setsandsteps.core.domain.repository.FileRepository
import com.heveamobile.setsandsteps.core.domain.repository.StepDataRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.repository.UserRepository
import com.heveamobile.setsandsteps.core.domain.source.HealthDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// DataStore must be a process-level singleton to avoid "multiple DataStores active" error.
// We cache it here so it survives Koin module reloads during database import.
private var dataStoreInstance: DataStore<Preferences>? = null

const val PREFS_FILE_NAME = "user_prefs.preferences_pb"

val coreDataModule = module {
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

    singleOf(::StepDataRepositoryImpl) { bind<StepDataRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::CardSetRepositoryImpl) { bind<CardSetRepository>() }
    singleOf(::CollectableCardRepositoryImpl) { bind<CollectableCardRepository>() }
    singleOf(::UserPreferencesRepositoryImpl) { bind<UserPreferencesRepository>() }

    singleOf(::AndroidFilePickerHandler) { bind<FilePickerHandler>() }
    singleOf(::AndroidFileRepositoryImpl) { bind<FileRepository>() }

    singleOf(::AndroidPermissionManager) { bind<PermissionManager>() }
    singleOf(::AndroidDailyReminderManager) { bind<DailyReminderManager>() }
    singleOf(::HealthConnectManager) { bind<HealthDataSource>() }
}
