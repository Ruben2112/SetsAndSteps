package com.heveamobile.setsandsteps.core.domain.manager

import com.heveamobile.setsandsteps.core.domain.model.CardSetDownloadState
import kotlinx.coroutines.flow.Flow

interface CardSetDownloadCoordinator {
    fun downloadCardSet(setId: String)
    fun updateCardSet(setId: String)
    fun observeDownloadState(setId: String): Flow<CardSetDownloadState>
}
