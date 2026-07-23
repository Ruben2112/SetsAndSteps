package com.heveamobile.setsandsteps.core.data.source.android

import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.heveamobile.setsandsteps.core.database.AppDatabase
import com.heveamobile.setsandsteps.core.database.DATABASE_FILE_NAME
import com.heveamobile.setsandsteps.core.database.di.coreDatabaseModule
import com.heveamobile.setsandsteps.core.data.di.PREFS_FILE_NAME
import com.heveamobile.setsandsteps.core.data.di.coreDataModule
import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler
import com.heveamobile.setsandsteps.core.domain.repository.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class AndroidFileRepositoryImpl(
    private val context: Context,
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>,
    private val filePickerHandler: FilePickerHandler,
) : FileRepository {

    override suspend fun exportProgress(): Result<Unit> = runCatching {
        val dbFile = context.getDatabasePath(DATABASE_FILE_NAME)
        val walFile = context.getDatabasePath("$DATABASE_FILE_NAME-wal")
        val shmFile = context.getDatabasePath("$DATABASE_FILE_NAME-shm")
        val prefsFile = context.filesDir.resolve(PREFS_FILE_NAME)

        val destinationUriString =
            filePickerHandler.getExportLocation("sets_and_steps_progress.mbs")
            ?: throw Exception("User cancelled export")
        val destinationUri = destinationUriString.toUri()

        withContext(Dispatchers.IO) {
            // Force a checkpoint. This ensures all WAL data is in the .db file.
            val cursor = database.query(
                "PRAGMA wal_checkpoint(FULL)",
                null,
            )
            cursor.moveToFirst()
            cursor.close()

            context.contentResolver
                .openOutputStream(destinationUri)
                ?.use { outputStream ->
                    ZipOutputStream(outputStream).use { zipOut ->
                        // Add Database
                        addToZip(
                            zipOut,
                            dbFile,
                            DATABASE_FILE_NAME,
                        )

                        // Add Preferences
                        if (prefsFile.exists()) {
                            addToZip(
                                zipOut,
                                prefsFile,
                                PREFS_FILE_NAME,
                            )
                        }

                        // Add WAL/SHM if they exist after checkpoint
                        if (walFile.exists()) addToZip(
                            zipOut,
                            walFile,
                            "$DATABASE_FILE_NAME-wal",
                        )
                        if (shmFile.exists()) addToZip(
                            zipOut,
                            shmFile,
                            "$DATABASE_FILE_NAME-shm",
                        )
                    }
                }
                ?: throw Exception("Could not open output stream")
        }
    }

    private fun addToZip(
        zipOut: ZipOutputStream,
        file: File,
        entryName: String,
    ) {
        FileInputStream(file).use { fis ->
            val zipEntry = ZipEntry(entryName)
            zipOut.putNextEntry(zipEntry)
            fis.copyTo(zipOut)
            zipOut.closeEntry()
        }
    }

    override suspend fun importProgress(): Result<Unit> = runCatching {
        val dbFile = context.getDatabasePath(DATABASE_FILE_NAME)
        val walFile = context.getDatabasePath("$DATABASE_FILE_NAME-wal")
        val shmFile = context.getDatabasePath("$DATABASE_FILE_NAME-shm")

        val sourceUriString = filePickerHandler.getImportLocation()
            ?: throw Exception("User cancelled import")
        val sourceUri = sourceUriString.toUri()

        withContext(Dispatchers.IO) {
            // We close the database to allow overwriting the file
            database.close()

            context.contentResolver
                .openInputStream(sourceUri)
                ?.use { inputStream ->
                    ZipInputStream(inputStream).use { zipIn ->
                        var entry = zipIn.nextEntry
                        while (entry != null) {
                            when (entry.name) {
                                DATABASE_FILE_NAME -> {
                                    FileOutputStream(dbFile).use { zipIn.copyTo(it) }
                                }

                                "$DATABASE_FILE_NAME-wal" -> {
                                    FileOutputStream(walFile).use { zipIn.copyTo(it) }
                                }

                                "$DATABASE_FILE_NAME-shm" -> {
                                    FileOutputStream(shmFile).use { zipIn.copyTo(it) }
                                }

                                PREFS_FILE_NAME -> {
                                    // Live-update DataStore instead of overwriting the file
                                    val tempFile = File(
                                        context.cacheDir,
                                        "temp_prefs.preferences_pb",
                                    )
                                    FileOutputStream(tempFile).use { zipIn.copyTo(it) }

                                    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                                    try {
                                        val tempStore =
                                            PreferenceDataStoreFactory.create(scope = scope) { tempFile }
                                        val newPrefs = tempStore.data.first()

                                        dataStore.edit { currentPrefs ->
                                            currentPrefs.clear()
                                            newPrefs
                                                .asMap()
                                                .forEach { (key, value) ->
                                                    @Suppress("UNCHECKED_CAST")
                                                    currentPrefs[key as Preferences.Key<Any>] =
                                                        value
                                                }
                                        }
                                    } finally {
                                        scope.cancel()
                                        tempFile.delete()
                                    }
                                }
                            }

                            zipIn.closeEntry()
                            entry = zipIn.nextEntry
                        }
                    }
                }
                ?: throw Exception("Could not open input stream")

            // Reload Koin modules to refresh the database instance and repositories
            val modulesToReload = listOf(coreDatabaseModule, coreDataModule)
            unloadKoinModules(modulesToReload)
            loadKoinModules(modulesToReload)
        }
    }
}
