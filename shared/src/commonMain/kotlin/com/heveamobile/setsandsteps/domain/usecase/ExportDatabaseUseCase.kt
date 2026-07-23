package com.heveamobile.setsandsteps.domain.usecase

import com.heveamobile.setsandsteps.domain.repository.FileRepository

class ExportDatabaseUseCase(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return fileRepository.exportProgress()
    }
}
