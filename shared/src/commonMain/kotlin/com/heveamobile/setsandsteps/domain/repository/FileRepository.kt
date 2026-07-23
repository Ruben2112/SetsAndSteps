package com.heveamobile.setsandsteps.domain.repository

interface FileRepository {
    suspend fun exportProgress(): Result<Unit>
    suspend fun importProgress(): Result<Unit>
}