package com.heveamobile.setsandsteps.feature.sets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.manager.CardSetDownloadCoordinator
import com.heveamobile.setsandsteps.core.domain.model.CardSetDownloadState
import com.heveamobile.setsandsteps.core.domain.repository.CardSetCatalogRepository
import com.heveamobile.setsandsteps.core.domain.usecase.GetCatalogCardSetsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetSetsWithProgressUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetUserUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.ToggleSetActiveStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetsViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getSetsWithProgressUseCase: GetSetsWithProgressUseCase,
    private val getCatalogCardSetsUseCase: GetCatalogCardSetsUseCase,
    private val cardSetCatalogRepository: CardSetCatalogRepository,
    private val cardSetDownloadCoordinator: CardSetDownloadCoordinator,
    private val toggleSetActiveStateUseCase: ToggleSetActiveStateUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SetsState())
    val state: StateFlow<SetsState> = _state.asStateFlow()

    private val _events = Channel<SetsEvent>()
    val events = _events.receiveAsFlow()

    // Tracks sets tapped for purchase whose ownership hasn't landed locally yet, so we know
    // to fire a one-time "Purchase succeeded" event the moment the set appears in `sets`.
    private val pendingPurchaseSetIds = MutableStateFlow<Set<String>>(emptySet())

    init {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getUserUseCase().collectLatest { user ->
//                _state.update {
//                    it.copy(
//                        availableSteps = user?.availableSteps
//                            ?: 0L,
//                    )
//                }
            }
        }

        val ownedSetsFlow = getSetsWithProgressUseCase()
        val catalogSetsFlow = getCatalogCardSetsUseCase()

        viewModelScope.launch(Dispatchers.IO) {
            combine(
                ownedSetsFlow,
                catalogSetsFlow,
                cardSetCatalogRepository.getRemoteCardSetsFlow(),
            ) { owned, catalog, remote ->
                Triple(
                    owned,
                    catalog,
                    remote,
                )
            }
                .onStart {
                    _state.update { it.copy(isLoading = true) }
                }
                .collectLatest { (owned, catalog, remote) ->
                    val remoteVersionById = remote.associateBy { it.id }
                    val updateAvailableIds = owned
                        .mapNotNull { set ->
                            val remoteVersion = remoteVersionById[set.id]?.version
                            if (remoteVersion != null && remoteVersion > set.version) set.id else null
                        }
                        .toSet()

                    val previousOwnedIds = _state.value.sets
                        .map { it.id }
                        .toSet()
                    val newlyPurchased = owned.filter {
                        it.id !in previousOwnedIds && it.id in pendingPurchaseSetIds.value
                    }
                    if (newlyPurchased.isNotEmpty()) {
                        pendingPurchaseSetIds.update {
                            it - newlyPurchased
                                .map { set -> set.id }
                                .toSet()
                        }
                        newlyPurchased.forEach { set ->
                            _events.send(SetsEvent.PurchaseSucceeded(set.name))
                        }
                    }

                    _state.update { state ->
                        state.copy(
                            sets = owned,
                            catalogSets = catalog,
                            updateAvailableSetIds = updateAvailableIds,
                            expandedSetId = state.expandedSetId
                                ?: owned.firstOrNull()?.id,
                            isLoading = false,
                        )
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            combine(
                ownedSetsFlow,
                catalogSetsFlow,
            ) { owned, catalog ->
                (owned.map { it.id } + catalog.map { it.id }).toSet()
            }
                .distinctUntilChanged()
                .flatMapLatest { ids ->
                    if (ids.isEmpty()) {
                        flowOf(emptyMap())
                    } else {
                        combine(
                            ids.map { id ->
                                cardSetDownloadCoordinator
                                    .observeDownloadState(id)
                                    .map { id to it }
                            },
                        ) { pairs -> pairs.toMap() }
                    }
                }
                .collectLatest { newDownloadStates ->
                    val previousDownloadStates = _state.value.downloadStates
                    newDownloadStates.forEach { (setId, downloadState) ->
                        val previousState = previousDownloadStates[setId]

                        if (downloadState == CardSetDownloadState.Failed && previousState != CardSetDownloadState.Failed) {
                            val setName =
                                (_state.value.sets + _state.value.catalogSets).firstOrNull { it.id == setId }?.name
                                    ?: return@forEach
                            val isOwnedSet = _state.value.sets.any { it.id == setId }
                            pendingPurchaseSetIds.update { it - setId }
                            _events.send(
                                if (isOwnedSet) {
                                    SetsEvent.UpdateFailed(setName)
                                } else {
                                    SetsEvent.DownloadFailed(setName)
                                },
                            )
                        }
                    }
                    _state.update { it.copy(downloadStates = newDownloadStates) }
                }
        }
    }

    fun onAction(action: SetsAction) {
        when (action) {
            is SetsAction.ViewProgress -> TODO()
            is SetsAction.ExpandProgress -> {
                _state.update {
                    it.copy(
                        expandedSetId = if (it.expandedSetId != action.set.id) action.set.id else null,
                    )
                }
            }

            is SetsAction.SelectTab -> {
                _state.update { it.copy(selectedTab = action.tab) }
            }

            is SetsAction.DownloadSet -> {
                pendingPurchaseSetIds.update { it + action.setId }
                cardSetDownloadCoordinator.downloadCardSet(action.setId)
            }

            is SetsAction.UpdateSet -> {
                cardSetDownloadCoordinator.updateCardSet(action.setId)
            }

            is SetsAction.ToggleActiveState -> {
                viewModelScope.launch(Dispatchers.IO) {
                    toggleSetActiveStateUseCase(action.set.id)
                    _events.send(
                        SetsEvent.ActiveStateToggled(
                            setName = action.set.name,
                            isActive = action.set.userData?.isActive?.not()
                                ?: true,
                        ),
                    )
                }
            }
        }
    }
}
