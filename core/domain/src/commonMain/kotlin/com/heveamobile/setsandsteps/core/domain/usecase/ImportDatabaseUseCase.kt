package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.repository.FileRepository

class ImportDatabaseUseCase(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return fileRepository.importProgress()
    }
}
