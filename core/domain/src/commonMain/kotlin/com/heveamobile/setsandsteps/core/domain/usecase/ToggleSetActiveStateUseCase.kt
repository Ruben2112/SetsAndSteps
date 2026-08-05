package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.repository.CardSetRepository

class ToggleSetActiveStateUseCase(
    private val cardSetRepository: CardSetRepository,
) {
    suspend operator fun invoke(setId: String) {
        cardSetRepository.toggleActiveState(setId)
    }
}
