package com.heveamobile.setsandsteps.ui.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heveamobile.setsandsteps.core.domain.usecase.GetSetsWithProgressUseCase
import com.heveamobile.setsandsteps.core.domain.usecase.GetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetsViewModel(
    val getUserUseCase: GetUserUseCase,
    val getSetsWithProgressUseCase: GetSetsWithProgressUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SetsState())
    val state: StateFlow<SetsState> = _state.asStateFlow()

    init {
        _state.update { it.copy(isLoading = true) }

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
            getSetsWithProgressUseCase()
                .onStart {
                    _state.update { it.copy(isLoading = true) }
                }
                .collectLatest { sets ->
                    _state.update { state ->
                        state.copy(
                            sets = sets,
                            expandedSetId = state.expandedSetId
                                ?: sets.first().id,
                            isLoading = false,
                        )
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().first()
            getSetsWithProgressUseCase().first()

            _state.update { it.copy(isLoading = false) }
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
        }
    }
}