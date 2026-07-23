package com.heveamobile.setsandsteps.core.domain.usecase

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FoundCardsHandler {
    private val _foundCardsEvents = MutableSharedFlow<SpendStepsResult>(extraBufferCapacity = 1)
    val foundCardsEvents = _foundCardsEvents.asSharedFlow()

    fun announceFoundCards(result: SpendStepsResult) {
        _foundCardsEvents.tryEmit(result)
    }
}
