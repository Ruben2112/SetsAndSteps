package com.heveamobile.setsandsteps.core.domain.repository

interface FileRepository {
    suspend fun exportProgress(): Result<Unit>
    suspend fun importProgress(): Result<Unit>
}