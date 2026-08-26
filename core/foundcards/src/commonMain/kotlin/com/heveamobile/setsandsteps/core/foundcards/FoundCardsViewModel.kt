package com.heveamobile.setsandsteps.core.foundcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.request.ImageResult
import com.heveamobile.setsandsteps.core.domain.model.ObtainPacksResult
import com.heveamobile.setsandsteps.core.domain.model.ObtainSinglesResult
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsEvent
import com.heveamobile.setsandsteps.core.domain.usecase.FoundCardsHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoundCardsViewModel(
    private val foundCardsHandler: FoundCardsHandler,
    private val imageLoader: ImageLoader,
    private val platformContext: PlatformContext,
) : ViewModel() {
    private val _state = MutableStateFlow(FoundCardsState())
    val state: StateFlow<FoundCardsState> = _state.asStateFlow()

    private val singlesController = FoundCardsSinglesController(
        viewModelScope,
        _state,
    )
    private val packOpeningController = FoundCardsPackOpeningController(
        viewModelScope,
        _state,
    )

    init {
        viewModelScope.launch {
            foundCardsHandler.foundCardsEvents.collectLatest { event ->
                when (event) {
                    FoundCardsEvent.Loading -> _state.update { FoundCardsState(isLoading = true) }

                    FoundCardsEvent.Cleared -> _state.update { FoundCardsState() }

                    is FoundCardsEvent.Result -> {
                        val result = event.result
                        viewModelScope.launch {
                            // 1. Collect all URLs to pre-fetch
                            val urls = mutableSetOf<String>()
                            when (result) {
                                is ObtainSinglesResult -> {
                                    result.allCards.forEach { foundCard ->
                                        foundCard.card.imageUrl?.let { urls.add(it) }
                                        foundCard.cardSet.backsideImageUrl?.let { urls.add(it) }
                                    }
                                }

                                is ObtainPacksResult -> {
                                    result.setResults.forEach { setResult ->
                                        setResult.cardSet.backsideImageUrl?.let { urls.add(it) }
                                        setResult.packs.forEach { pack ->
                                            pack.forEach { foundCard ->
                                                foundCard.card.imageUrl?.let { urls.add(it) }
                                            }
                                        }
                                    }
                                }
                            }

                            // 2. Pre-fetch images in parallel
                            val deferreds: List<Deferred<ImageResult>> = urls.map { url ->
                                async {
                                    val request = ImageRequest
                                        .Builder(platformContext)
                                        .data(url)
                                        .build()
                                    imageLoader.execute(request)
                                }
                            }
                            deferreds.awaitAll()

                            // 3. Update state once all images are fetched (or failed)
                            _state.update {
                                when (result) {
                                    is ObtainSinglesResult -> FoundCardsState(
                                        singlesState = SinglesUiState(
                                            foundCards = result.allCards.sortedBy { it.card.rarity },
                                            mapPointsGained = result.totalSetPointsGained,
                                        ),
                                    )

                                    is ObtainPacksResult -> FoundCardsState(
                                        packOpeningState = PackOpeningUiState(
                                            setPages = result.setResults.map { setResult ->
                                                SetPageUiState(
                                                    cardSet = setResult.cardSet,
                                                    packs = setResult.packs.map { pack -> PackUiState(cards = pack) },
                                                    setPointsGained = setResult.setPointsGained,
                                                )
                                            },
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: FoundCardsAction) {
        when (action) {
            is FoundCardsAction.Shared.CloseFoundCards -> {
                _state.update { FoundCardsState() }
            }

            is FoundCardsAction.Shared.ToggleCardInfo -> {
                val cardShown = _state.value.cardShown
                viewModelScope.launch {
                    _state.update { state ->
                        state.copy(
                            cardShown = if (cardShown == null) action.card else null,
                        )
                    }
                }
            }

            is FoundCardsAction.Singles -> singlesController.onAction(action)

            is FoundCardsAction.PackOpening -> packOpeningController.onAction(action)
        }
    }
}
