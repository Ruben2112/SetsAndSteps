package com.heveamobile.setsandsteps.domain.repository

interface FilePickerHandler {
    suspend fun getExportLocation(fileName: String): String?
    suspend fun getImportLocation(): String?
}
