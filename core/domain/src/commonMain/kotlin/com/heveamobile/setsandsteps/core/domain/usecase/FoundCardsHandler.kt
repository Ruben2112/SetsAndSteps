package com.heveamobile.setsandsteps.core.domain.usecase

import com.heveamobile.setsandsteps.core.domain.model.ObtainCardsResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface FoundCardsEvent {
    data object Loading : FoundCardsEvent
    data object Cleared : FoundCardsEvent
    data class Result(val result: ObtainCardsResult) : FoundCardsEvent
}

class FoundCardsHandler {
    private val _foundCardsEvents = MutableSharedFlow<FoundCardsEvent>(extraBufferCapacity = 2)
    val foundCardsEvents = _foundCardsEvents.asSharedFlow()

    fun announceLoading() {
        _foundCardsEvents.tryEmit(FoundCardsEvent.Loading)
    }

    fun announceFoundCards(result: ObtainCardsResult) {
        _foundCardsEvents.tryEmit(FoundCardsEvent.Result(result))
    }

    fun clearLoading() {
        _foundCardsEvents.tryEmit(FoundCardsEvent.Cleared)
    }
}
