package com.heveamobile.setsandsteps.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.manager.PermissionManager
import com.heveamobile.setsandsteps.core.domain.manager.PermissionStatus
import com.heveamobile.setsandsteps.core.domain.manager.PermissionType
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
import com.heveamobile.setsandsteps.core.domain.usecase.GetSetsWithProgressUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetUserUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.SpendStepsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.SyncStepsUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.UpsertInitialMapDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val permissionManager: PermissionManager,
    private val getUserUseCase: GetUserUseCase,
    private val syncStepsUseCase: SyncStepsUseCase,
    private val upsertInitialCardSetDataUseCase: UpsertInitialMapDataUseCase,
    private val getCardSetsWithProgressUseCase: GetSetsWithProgressUseCase,
    private val spendStepsUseCase: SpendStepsUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val foundCardsHandler: FoundCardsHandler,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        _state.update { it.copy(isLoadingSteps = true) }

        viewModelScope.launch {
            getUserUseCase().collectLatest { user ->
                _state.update {
                    it.copy(
                        availableSteps = user?.availableSteps
                            ?: 0L,
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            combine(
                getCardSetsWithProgressUseCase(),
                userPreferencesRepository.distanceMultiplier,
            ) { sets, multiplier -> sets to multiplier }.collectLatest { (sets, multiplier) ->
                val calculatedDistance = sets.firstOrNull()?.userData?.calculatedDistance
                    ?: 0L
                val requiredSteps = (calculatedDistance * multiplier).toLong()

                _state.update { state ->
                    state.copy(
                        requiredSteps = requiredSteps,
                    )
                }
            }
        }

        viewModelScope.launch {
            combine(
                userPreferencesRepository.hideUndiscovered,
                userPreferencesRepository.gridSortingOrder,
            ) { hide, order -> hide to order }.collectLatest { (hide, order) ->
                _state.update { state ->
                    state.copy(
                        sharedCardsState = state.sharedCardsState.copy(
                            hideUndiscovered = hide,
                            sortingOrder = order,
                        ),
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val syncStepsJob = launch {
                if (permissionManager.checkPermissionStatus(PermissionType.Health) == PermissionStatus.Granted) {
                    syncStepsUseCase()
                }
            }

            val upsertInitialCardSetDataJob = launch {
                upsertInitialCardSetDataUseCase()
            }

            joinAll(
                syncStepsJob,
                upsertInitialCardSetDataJob,
            )

            val hideUndiscovered = userPreferencesRepository.hideUndiscovered.first()
            val sortingOrder = userPreferencesRepository.gridSortingOrder.first()

            getUserUseCase().first()
            getCardSetsWithProgressUseCase().first()

            _state.update {
                it.copy(
                    isLoadingSteps = false,
                    sharedCardsState = it.sharedCardsState.copy(
                        hideUndiscovered = hideUndiscovered,
                        sortingOrder = sortingOrder,
                    ),
                )
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OpenNavigationDrawer -> {
                _state.update { it.copy(isDrawerOpen = true) }
            }

            HomeAction.CloseNavigationDrawer -> {
                _state.update { it.copy(isDrawerOpen = false) }
            }

            HomeAction.SyncSteps -> {
                viewModelScope.launch {
                    syncStepsUseCase()
                }
            }

            HomeAction.SpendSteps -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = spendStepsUseCase()
                    if (result.cards.isNotEmpty()) {
                        foundCardsHandler.announceFoundCards(result)
                    }
                }
            }

            is HomeAction.ToggleDropdownMenu -> {
                _state.update { state ->
                    state.copy(
                        sharedCardsState = state.sharedCardsState.copy(
                            showDropdownMenu = !state.sharedCardsState.showDropdownMenu,
                        ),
                    )
                }
            }

            is HomeAction.ToggleHideUndiscovered -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateHideUndiscovered(
                        !userPreferencesRepository.hideUndiscovered.first(),
                    )
                }
            }

            is HomeAction.UpdateSortOrder -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateGridSortingOrder(action.sortOrder)
                }
            }
        }
    }
}