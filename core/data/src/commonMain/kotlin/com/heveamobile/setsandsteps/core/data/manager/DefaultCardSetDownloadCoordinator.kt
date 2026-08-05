package com.heveamobile.setsandsteps.core.data.manager

import com.heveamobile.setsandsteps.core.domain.manager.CardSetDownloadCoordinator
import com.heveamobile.setsandsteps.core.domain.model.CardSetDownloadState
import com.heveamobile.setsandsteps.core.domain.repository.CardSetCatalogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DefaultCardSetDownloadCoordinator(
    private val cardSetCatalogRepository: CardSetCatalogRepository,
    private val applicationScope: CoroutineScope,
) : CardSetDownloadCoordinator {

    private val downloadStates = mutableMapOf<String, MutableStateFlow<CardSetDownloadState>>()

    override fun downloadCardSet(setId: String) {
        runIfNotInProgress(setId) { cardSetCatalogRepository.downloadCardSet(setId) }
    }

    override fun updateCardSet(setId: String) {
        runIfNotInProgress(setId) { cardSetCatalogRepository.updateCardSet(setId) }
    }

    override fun observeDownloadState(setId: String): Flow<CardSetDownloadState> {
        return stateFlowFor(setId).asStateFlow()
    }

    private fun runIfNotInProgress(
        setId: String,
        block: suspend () -> Result<Unit>,
    ) {
        val state = stateFlowFor(setId)
        if (state.value == CardSetDownloadState.InProgress) return

        state.value = CardSetDownloadState.InProgress
        applicationScope.launch {
            val result = block()
            state.value = if (result.isSuccess) {
                CardSetDownloadState.Idle
            } else {
                CardSetDownloadState.Failed
            }
        }
    }

    private fun stateFlowFor(setId: String): MutableStateFlow<CardSetDownloadState> {
        return downloadStates.getOrPut(setId) { MutableStateFlow(CardSetDownloadState.Idle) }
    }
}
