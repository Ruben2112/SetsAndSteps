package com.heveamobile.setsandsteps.core.domain.repository

interface FilePickerHandler {
    suspend fun getExportLocation(fileName: String): String?
    suspend fun getImportLocation(): String?

    fun setListeners(
        onExport: (String) -> Unit,
        onImport: () -> Unit,
    )

    fun onResult(
        uri: String?,
        isExport: Boolean,
    )
}
