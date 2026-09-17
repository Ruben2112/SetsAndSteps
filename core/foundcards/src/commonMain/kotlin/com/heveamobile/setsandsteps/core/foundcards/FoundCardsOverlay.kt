package com.heveamobile.setsandsteps.core.foundcards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.heveamobile.setsandsteps.core.designsystem.component.FlipOverTime
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.domain.manager.Vibrator
import com.heveamobile.setsandsteps.core.domain.model.Rarity
import com.heveamobile.setsandsteps.core.domain.repository.UserPreferencesRepository
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.Res
import com.heveamobile.setsandsteps.core.foundcards.generated.resources.close_screen_button
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun FoundCardsOverlay(
    modifier: Modifier = Modifier,
    state: FoundCardsState,
    onAction: (FoundCardsAction) -> Unit,
) {
    val vibrator = koinInject<Vibrator>()
    val userPreferencesRepository = koinInject<UserPreferencesRepository>()
    val vibrationIsEnabled by userPreferencesRepository.isVibrationEnabled.collectAsStateWithLifecycle(true)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5F))
            .pointerInput(Unit) {
                detectTapGestures {
                    /* Consumes tap events so they don't reach the UI behind */
                }
            },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        AnimatedContent(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues
                        .calculateBottomPadding(),
                ),
            targetState = state.cardShown,
            transitionSpec = {
                // Smooth fade when opening/closing the detail view
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
        ) { card ->
            if (card != null) {
                CardDetailContent(
                    state = state,
                    card = card,
                )
            } else {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val density = LocalDensity.current
                    val height = with(density) { maxHeight.roundToPx() }
                    val width = with(density) { maxWidth.roundToPx() }

                    if (state.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                strokeWidth = 4.dp,
                            )
                        }
                    } else if (state.isPackOpening) {
                        state.packOpeningState?.let { packOpeningState ->
                            if (packOpeningState.showSummaryScreen) {
                                PackOpeningSummaryGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    packOpeningState = packOpeningState,
                                    onAction = onAction,
                                )
                            } else {
                                PackOpeningPager(
                                    modifier = Modifier.fillMaxSize(),
                                    packOpeningState = packOpeningState,
                                    onAction = onAction,
                                    onRevealStarted = { card ->
                                        if (vibrationIsEnabled && card.rarity != Rarity.Common) {
                                            val timings = mutableListOf<Long>()
                                            val amplitudes = mutableListOf<Int>()

                                            // The card performs a full 360-degree rotation for each rarity tier
                                            // below its target, and a final 180-degree flip for the target rarity.
                                            for (i in 1 until card.rarity.intValue) {
                                                timings.add(FlipOverTime * 2)
                                                amplitudes.add(Rarity.fromInt(i).vibrationAmplitude)
                                            }

                                            // Final half-rotation for the card's actual rarity
                                            timings.add(FlipOverTime)
                                            amplitudes.add(card.rarity.vibrationAmplitude)

                                            vibrator.vibrateWaveform(
                                                timings.toLongArray(),
                                                amplitudes.toIntArray(),
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    } else {
                        state.singlesState?.let { singlesState ->
                            SinglesGrid(
                                singlesState = singlesState,
                                onAction = onAction,
                                screenHeight = height,
                                screenWidth = width,
                            )
                        }
                    }
                }
            }
        }

        val showCloseButton = state.cardShown != null || if (state.isPackOpening) {
            state.packOpeningState?.showSummaryScreen == true
        } else {
            state.singlesState?.foundCards
                .orEmpty()
                .none { !it.isRevealed }
        }

        AnimatedVisibility(
            visible = showCloseButton,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .padding(bottom = MaterialTheme.spacing.small),
                contentAlignment = Alignment.BottomCenter,
            ) {
                FloatingActionButton(
                    onClick = {
                        if (state.cardShown != null) {
                            onAction(FoundCardsAction.Shared.ToggleCardInfo(null))
                        } else {
                            onAction(FoundCardsAction.Shared.CloseFoundCards)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(Res.string.close_screen_button),
                        modifier = Modifier.size(MaterialTheme.spacing.large),
                    )
                }
            }
        }

        if (state.isPackOpening) {
            state.packOpeningState?.let { packOpeningState ->
                PackOpeningFloatingActions(
                    packOpeningState = packOpeningState,
                    cardDetailsShown = state.cardShown != null,
                    onAction = onAction,
                    bottomPadding = paddingValues.calculateBottomPadding(),
                )
            }
        } else {
            state.singlesState?.let { singlesState ->
                SinglesFloatingActions(
                    singlesState = singlesState,
                    onAction = onAction,
                    bottomPadding = paddingValues.calculateBottomPadding(),
                )
            }
        }
    }
}
