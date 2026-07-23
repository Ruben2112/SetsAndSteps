package com.heveamobile.setsandsteps.core.data.source.android

import com.heveamobile.setsandsteps.core.domain.repository.FilePickerHandler
import kotlinx.coroutines.CompletableDeferred

class AndroidFilePickerHandler : FilePickerHandler {
    private var exportDeferred: CompletableDeferred<String?>? = null
    private var importDeferred: CompletableDeferred<String?>? = null

    private var onExportRequested: ((String) -> Unit)? = null
    private var onImportRequested: (() -> Unit)? = null

    override suspend fun getExportLocation(fileName: String): String? {
        val deferred = CompletableDeferred<String?>()
        exportDeferred = deferred
        onExportRequested?.invoke(fileName)
        return deferred.await()
    }

    override suspend fun getImportLocation(): String? {
        val deferred = CompletableDeferred<String?>()
        importDeferred = deferred
        onImportRequested?.invoke()
        return deferred.await()
    }

    override fun setListeners(
        onExport: (String) -> Unit,
        onImport: () -> Unit,
    ) {
        onExportRequested = onExport
        onImportRequested = onImport
    }

    override fun onResult(
        uri: String?,
        isExport: Boolean,
    ) {
        if (isExport) {
            exportDeferred?.complete(uri)
            exportDeferred = null
        } else {
            importDeferred?.complete(uri)
            importDeferred = null
        }
    }
}
