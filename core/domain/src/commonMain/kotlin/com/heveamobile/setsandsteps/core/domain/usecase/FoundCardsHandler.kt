package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.ObtainCardsResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FoundCardsHandler {
    private val _foundCardsEvents = MutableSharedFlow<ObtainCardsResult>(extraBufferCapacity = 1)
    val foundCardsEvents = _foundCardsEvents.asSharedFlow()

    fun announceFoundCards(result: ObtainCardsResult) {
        _foundCardsEvents.tryEmit(result)
    }
}
